package ps.spaceinvaders;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;

public class InvadersGameView extends SurfaceView implements Runnable {

    private boolean mode;

    Context context;

    private Thread mainTh;
    UpdateEnemiesThread enemiesThread;
    BulletManagerThread bulletThread;
    SpawningThread firstSpawnTh;
    SpawningThread2 secondSpawnTh;
    SpawningThread3 thirdSpawnTh;

    private SurfaceHolder holder;

    private volatile boolean isPlaying;

    private boolean isPaused;

    private Canvas canvas;
    private Paint paint;

    private int screenX;
    private int screenY;

    private long fps;
    private long timeFrame;
    private long timer;
    private long spawnTimer;
    private  int increments = 1;

    //Elementos del juego
    private ArrayList<Enemy> enemiesList = new ArrayList();
    private ArrayList<Enemy> spawnedEnemies = new ArrayList();
    private SpaceShip spaceShip;
    private Defence[] blocks = new Defence[400];

    //Controlar las balas
    private ArrayList<Bullet> bullets = new ArrayList();
    private ArrayList<Bullet> removedBullets = new ArrayList();
    //Las naves no pueden diparar mas N balas por vez
    private boolean fullCapacity;
    private int enemyBulletsCount;
    private int maxEnemyBullets = 10;

    int totalEnemies = 0;
    int killedEnemies = 0;
    private int numDefences;
    private boolean firstSpawn;
    private int spawnCount;
    private Enemy lastSpawned;
    private long jumpTimer;

    //Puntuacion
    int score = 0;
    boolean lost = false;

    Handler handler = new Handler();
    boolean isReloading = false;

    private boolean animation = true;
    private long timeAnim = 1000;

    private long lastTime = System.currentTimeMillis();

    private boolean changeColor=false;

    private Bitmap bulletBitmap, enemyAnim1Bitmap,enemyAnim2Bitmap,enemyAnim3Bitmap,enemyAnim4Bitmap, spaceshipBitmap;

    //Botones de movimiento y disparo
    private Buttons izq,der,dis,arr,abj;
    private String name;

    public InvadersGameView (Context context, int x, int y, boolean isViolent,String name){
        super(context);
        spaceshipBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship), x/10, y/10, false);
        bulletBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet1), x/20, y/20, false);
        enemyAnim1Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderstart), x/20, y/20, false);
        enemyAnim2Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderend), x/20, y/20, false);
        enemyAnim3Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderstart2), x/20, y/20, false);
        enemyAnim4Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderend2), x/20, y/20, false);

        izq=new Buttons(context,x,y,R.drawable.izq);
        der=new Buttons(context,x,y,R.drawable.der);
        dis=new Buttons(context,x,y,R.drawable.scope);
        arr=new Buttons(context,x,y,R.drawable.arr);
        abj=new Buttons(context,x,y,R.drawable.abj);

        this.context = context;
        this.name=name;

        this.mode = isViolent;

        holder = getHolder();
        paint = new Paint();

        screenX= x;
        screenY= y;

        isPaused = true;

        if (getRank(this,10).compareTo("-1")==0) {
            saveInfo(this);
        }
        iniLvl();
    }

    //THREADS QUE VAMOS A USAR

    //-----THREAD QUE SE ENCARGA DEL SPAWN----//
    class SpawningThread extends Thread {
        @Override
        public void run() {
            if(enemiesList.get(0).getRow() > 0 && enemiesList.get(0).getColumn() > 0) {
                Enemy e = new Enemy(context, 0, 0, screenX, screenY, enemyAnim1Bitmap, enemyAnim2Bitmap, enemyAnim3Bitmap, enemyAnim4Bitmap);
                e.setX(enemiesList.get(0).getX());
                e.setY(enemiesList.get(0).getY()-enemiesList.get(0).getHeight()
                        -enemiesList.get(0).getPadding()/2);
                e.setEnemySpeed(enemiesList.get(0).getEnemySpeed());
                e.setEnemyMoving(enemiesList.get(0).getEnemyMoving());
                e.setSpawned(true);
                totalEnemies++;
                spawnCount++;
                spawnedEnemies.add(e);
                enemiesList.add(e);
            }
        }
    }

    class SpawningThread2 extends Thread {
        @Override
        public void run() {
            if (enemiesList.get(0).getRow() > 0 && enemiesList.get(0).getColumn() > 0 && lastSpawned.getColumn() < 9) {
                Enemy e = new Enemy(context, 0, 0, screenX, screenY, enemyAnim1Bitmap, enemyAnim2Bitmap, enemyAnim3Bitmap, enemyAnim4Bitmap);
                e.setX(spawnedEnemies.get(spawnedEnemies.size()-1).getX() + lastSpawned.getLength() +
                        spawnedEnemies.get(spawnedEnemies.size()-1).getPadding());
                e.setY(spawnedEnemies.get(spawnedEnemies.size()-1).getY());
                e.setEnemySpeed(enemiesList.get(0).getEnemySpeed());
                e.setEnemyMoving(enemiesList.get(0).getEnemyMoving());
                e.setSpawned(true);
                totalEnemies++;
                spawnCount++;
                spawnedEnemies.add(e);
                enemiesList.add(e);
            }
        }
    }

    class SpawningThread3 extends Thread {
        @Override
        public void run() {
            if(enemiesList.get(0).getRow() > 0 && lastSpawned.getRow() > 0 && enemiesList.get(0).getColumn() > 4 && lastSpawned.getColumn() > 4) {
                Enemy e = new Enemy(context, 0, 0, screenX, screenY, enemyAnim1Bitmap, enemyAnim2Bitmap, enemyAnim3Bitmap, enemyAnim4Bitmap);
                e.setX(spawnedEnemies.get(spawnedEnemies.size()-1).getX()-((spawnedEnemies.get(spawnedEnemies.size()-1).getLength()
                        *(spawnCount-1))+(spawnedEnemies.get(spawnedEnemies.size()-1).getPadding()*(spawnCount-1))));
                e.setY(spawnedEnemies.get(spawnedEnemies.size()-1).getY() - spawnedEnemies.get(spawnedEnemies.size()-1).getHeight()
                        - spawnedEnemies.get(spawnedEnemies.size()-1).getPadding() / 2);
                e.setEnemySpeed(enemiesList.get(0).getEnemySpeed());
                e.setEnemyMoving(enemiesList.get(0).getEnemyMoving());
                e.setSpawned(true);
                totalEnemies++;
                spawnCount = 1;
                spawnedEnemies.add(e);
                enemiesList.add(e);
            }
        }
    }

    //--------THREAD QUE INICIALIZA LA PARTIDA--------//
    class LoadingThread extends Thread {
        @Override
        public void run() {
            try {
                jumpTimer = -1;
                lastSpawned = new Enemy(context, 0, 0, screenX, screenY, enemyAnim1Bitmap, enemyAnim2Bitmap, enemyAnim3Bitmap, enemyAnim4Bitmap);
                firstSpawn = false;
                spawnCount = 0;
                increments = 1;
                isReloading = false;
                spaceShip = new SpaceShip(context, screenX, screenY, spaceshipBitmap);
                spaceShip.resetShootsCount();
                changeColor = false;
                killedEnemies = 0;
                bullets.clear();
                enemiesList.clear();
                spawnedEnemies.clear();
                score = 0;

                fullCapacity = false;
                enemyBulletsCount = 0;

                // Construye las defensas
                numDefences= 0;
                for(int shelterNumber = 0; shelterNumber < 4; shelterNumber++){
                    for(int column = 0; column < 10; column ++ ) {
                        for (int row = 0; row < 5; row++) {
                            if (!(row>1 && (column>1&&column<8))) {
                                blocks[numDefences] = new Defence(row, column, shelterNumber, screenX, screenY);
                                numDefences++;
                            }

                        }
                    }
                }

                // Construye la formación enemiga
                //numEnemies = 0;
                for(int column = 0; column < 4; column ++ ){
                    for(int row = 0; row < 3; row ++ ){
                        Enemy e = new Enemy(context, row, column, screenX, screenY, enemyAnim1Bitmap, enemyAnim2Bitmap, enemyAnim3Bitmap, enemyAnim4Bitmap);
                        enemiesList.add(e);
                    }
                }

                totalEnemies = enemiesList.size();
                lost=false;
            }
            catch (Exception e) {
                System.out.println("Error while loading the game");
                e.printStackTrace();
            }
        }
    }


    //---------THREAD QUE SE ENCARGA DEL MOVIMIENTO DE LOS ALIENS---//
    class UpdateEnemiesThread extends Thread {
        @Override
        public void run() {
            try {
                boolean bumped = false;
                // Actualiza todos los enemies activos
                for (int i = 0; i < enemiesList.size(); i++) {
                    if(RectF.intersects(spaceShip.getRect(), enemiesList.get(i).getRect())){
                        lost = true;
                    }
                    enemiesList.get(i).angryEnemie(killedEnemies);
                        // Mueve enemy
                        enemiesList.get(i).update(fps);
                        checkAlienBlockCollision(enemiesList.get(i));
                        // ¿Quiere hacer un disparo?
                        if (!fullCapacity && enemiesList.get(i).randomShot(spaceShip.getX(),
                                spaceShip.getLength(), killedEnemies)) {
                            bulletBitmap = Bitmap.createScaledBitmap(bulletBitmap, screenX/20, screenY/20, false);
                            Bullet b = new Bullet(context, screenY, screenX, bulletBitmap);
                            b.setEnemyBullet(true);
                            b.setFriend(true);
                            bullets.add(b);
                            enemyBulletsCount++;
                            if (bullets.get(bullets.size() - 1).shoot(enemiesList.get(i).getX()
                                            + enemiesList.get(i).getLength() / 2,
                                    enemiesList.get(i).getY(), bullets.get(bullets.size() - 1).DOWN)) {
                                if (enemyBulletsCount == maxEnemyBullets) {
                                    fullCapacity = true;
                                }
                            }
                        }
                        if (enemiesList.get(i).getX() > screenX - enemiesList.get(i).getLength() || enemiesList.get(i).getX() < 0){
                            if(jumpTimer != -1) {
                                if(System.currentTimeMillis() > jumpTimer+1000) {
                                    bumped = true;
                                }
                                jumpTimer = System.currentTimeMillis();
                            }
                            else  {
                                bumped = true;
                            }
                        }
                }
                if(bumped){
                    // Mueve a todos los invaders hacia abajo y cambia la dirección
                    for(int i = 0; i < enemiesList.size(); i++){
                        enemiesList.get(i).enemyCicle();
                        // Han llegado abajo
                        if((enemiesList.get(i).getY() > screenY - screenY / 10)){
                            lost = true;
                        }
                    }
                    jumpTimer = System.currentTimeMillis();
                }

            }
            catch (Exception e) {
                System.out.println("Error moviendo aliens");
                e.printStackTrace();
            }
        }
    }

    //--------- THREAD ENCARGADO DE GESTIONAR LAS BALAS ------//
    class BulletManagerThread extends Thread{
        @Override
        public void run() {
            try {
                for (Bullet b : bullets) {

                    b.update(fps);

                    //Comprueba limites pantalla
                    if (b.getImpactPointY() < 0 || b.getImpactPointY() > screenY) {
                        b.changeDirection();
                        b.updateBounceCounts();
                        //Una bala solo puede rebotar 2 veces
                        if (b.getBounceCounts() == 2) {
                            removedBullets.add(b);
                        }
                        b.setFriend(false);
                    }

                    //Si la bala choca con los enemigos
                    checkEnemyCollision(b);

                    //Si la bala choca con los bloques
                    checkBlockCollision(b);

                    //Si la bala choca con el jugador
                    if (RectF.intersects(b.getRect(), spaceShip.getRect())) {
                        lost = true;
                    }
                }
            }
            catch (Exception e){
                System.out.println("Error gestionando las balas");
                e.printStackTrace();
            }
        }
    }

    void iniLvl(){
        LoadingThread load = new LoadingThread();
        load.run();
        enemiesThread = new UpdateEnemiesThread();
        bulletThread = new BulletManagerThread();
        firstSpawnTh = new SpawningThread();
        secondSpawnTh = new SpawningThread2();
        thirdSpawnTh = new SpawningThread3();
        spawnTimer = -1;
    }

    //Si la bala choca con los enemigos
    public void checkEnemyCollision(Bullet b) {
        for(int i = 0; i < enemiesList.size(); i++) {
            //if (enemiesList.get(i).getVisibility()) {
                if (!b.getFriend() && RectF.intersects(b.getRect(), enemiesList.get(i).getRect())) {
                    if(enemiesList.get(i).isSpawned) {
                        spawnedEnemies.remove(enemiesList.get(i));
                    }
                    enemiesList.remove(enemiesList.get(i));
                    removedBullets.add(b);
                    score = score + 100;
                    killedEnemies++;
                    checkVictory();
                }
            //}
        }
    }

    public void checkAlienBlockCollision(Enemy e) {
        for(int i = 0; i < numDefences; i++) {
            if(blocks[i].getActive()) {
                if(RectF.intersects(blocks[i].getRect(), e.getRect())) {
                    blocks[i].destoyDefence();
                }
            }
        }
    }

    public void checkBlockCollision(Bullet b){
        for(int i = 0; i < numDefences; i++){
            if(blocks[i].getActive()){
                RectF r = blocks[i].getRect();
                if(RectF.intersects(b.getRect(), r)){
                    //b.setInactive();
                    blocks[i].destoyDefence();
                    removedBullets.add(b);
                    if(b.getEnemyBullet()) {
                        changeColor =!changeColor;
                    }
                }
                if(RectF.intersects(r, spaceShip.getRect())) {
                    lost = true;
                }
            }
        }
    }

    public void checkVictory() {
        if(score == totalEnemies * 100){
            lost = true;
        }
    }

    public void playerShoot() {
        //bulletBitmap = Bitmap.createScaledBitmap(bulletBitmap, screenX/20, screenY/20, false);
        Bullet b = new Bullet(context, screenY, screenX, bulletBitmap);
        bullets.add(b);
        b.shoot(spaceShip.getX() + spaceShip.getLength() / 2, spaceShip.getY()
                - spaceShip.getHeight(), b.UP);
    }

    @Override
    public void run() {
        while(isPlaying) {
            long iniFrameTime = System.currentTimeMillis();

            if (!isPaused) {
                update();

                if((iniFrameTime - lastTime) > timeAnim){
                    lastTime = System.currentTimeMillis();

                    animation = !animation;
                }
            }
            if(!lost){
                draw();
            }
            else {
                drawR(this);
            }


            timeFrame = System.currentTimeMillis() - iniFrameTime;
            if (timeFrame >= 1) {
                fps = 1000 / timeFrame;
            }
        }
    }

    private void update(){
        if(!spawnedEnemies.isEmpty()){
            lastSpawned = spawnedEnemies.get(spawnedEnemies.size()-1);
        }
        //No lo toques, no sabes como funciona pero lo hace, estate quieto
        //Hace spawn una nave cada N segundos
        if(System.currentTimeMillis() >= spawnTimer+5000*increments) {
            if(spawnedEnemies.isEmpty()) {
                firstSpawnTh.run();
            }
            else if (spawnCount < 4) {
                secondSpawnTh.run();
            }
            else {
                thirdSpawnTh.run();
            }

            increments++;
        }
        // Mueve la nave espacial
        spaceShip.update(fps);
        //Llamada el thread que se encarga de los aliens
        enemiesThread.run();

        //Thread encargado de gestionar las balas y todas sus comprobaciones
        bulletThread.run();

        //Limpia las balas que han tocado algo
        bullets.removeAll(removedBullets);
        for(Bullet b : removedBullets) {
            if(b.getEnemyBullet()) {
                enemyBulletsCount--;
            }
        }
        removedBullets.clear();

        if (enemyBulletsCount < maxEnemyBullets) {
            fullCapacity = false;
        }

        if(System.currentTimeMillis() >= timer+2000){
            isReloading = false;
            spaceShip.resetShootsCount();
        }

        if(lost){
            saveInfoR(this,score,name);
            //drawR(this);
            isPaused = true;
            //iniLvl();

        }
    }


    private void draw(){
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();

            canvas.drawColor(Color.argb(255, 0, 0, 0));


            paint.setColor(Color.argb(255, 255, 255, 255));

            //Pintar la puntuación
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(50);
            canvas.drawText("Score: " + score, 30,50, paint);

            // Dibuja la nave espacial

            canvas.drawBitmap(izq.getBitmap(), screenX/20*1, screenY - 200, paint);
            canvas.drawBitmap(der.getBitmap(), screenX/20*5, screenY - 200, paint);
            canvas.drawBitmap(dis.getBitmap(), screenX/20*9, screenY - 200, paint);
            canvas.drawBitmap(arr.getBitmap(), screenX/20*13, screenY - 200, paint);
            canvas.drawBitmap(abj.getBitmap(), screenX/20*17, screenY - 200, paint);
            canvas.drawBitmap(spaceShip.getBitmap(), spaceShip.getX(), spaceShip.getY(), paint);

            // Dibuja las defensas no destruidas
            for(int i = 0; i < numDefences; i++){
                if(blocks[i].getActive()) {
                    canvas.drawRect(blocks[i].getRect(), paint);
                }
            }

            // Dibuja a los invaders
            for(int i = 0; i < enemiesList.size(); i++) {
                if (!changeColor) {
                    if (animation) {
                        canvas.drawBitmap(enemiesList.get(i).getBitmap(), enemiesList.get(i).getX(), enemiesList.get(i).getY(), paint);
                    } else {
                        canvas.drawBitmap(enemiesList.get(i).getBitmap2(), enemiesList.get(i).getX(), enemiesList.get(i).getY(), paint);
                    }
                } else {
                    if (animation) {
                        canvas.drawBitmap(enemiesList.get(i).getBitmap3(), enemiesList.get(i).getX(), enemiesList.get(i).getY(), paint);
                    } else {
                        canvas.drawBitmap(enemiesList.get(i).getBitmap4(), enemiesList.get(i).getX(), enemiesList.get(i).getY(), paint);
                    }

                }
            }

            // Dibuja las balas de los invaders
            if(!bullets.isEmpty()) {
                for (Bullet b : bullets) {
                    if (!b.getEnemyBullet()) {
                        canvas.drawBitmap(b.getBitmap(), b.getX() - b.getLength() / 2, b.getY(), paint);
                    } else {
                        canvas.drawBitmap(b.getBitmap(), b.getX() - b.getLength() / 2, b.getY(), paint);
                    }
                }
            }
            // Actualiza todas las balas de los invaders si están activas

            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        isPlaying = false;
    }

    public void resume(){
        isPlaying = true;
        mainTh = new Thread(this);
        mainTh.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        if(!lost) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                // El jugador ha pulsado la pantalla
                case MotionEvent.ACTION_DOWN:
                    isPaused = false;
                    if ((motionEvent.getX() > (screenX/20*1))&&(motionEvent.getX() < (screenX/20*1+izq.getLength())) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
                        spaceShip.setMovementState(spaceShip.LEFT); }
                    else if ((motionEvent.getX() > (screenX/20*5))&&(motionEvent.getX() < (screenX/20*5+izq.getLength())) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
                        spaceShip.setMovementState(spaceShip.RIGHT);
                    }
                    if ((motionEvent.getX() > (screenX/20*9))&&(motionEvent.getX() < (screenX/20*9+izq.getLength())) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
                        if(!isReloading) {
                            playerShoot();
                            spaceShip.addShootsCount();
                            timer = System.currentTimeMillis();
                            if(spaceShip.getShootsCount() >= 2) {
                                isReloading = true;
                            }
                        }
                    }
                    else if ((motionEvent.getX() > (screenX/20*13))&&(motionEvent.getX() < (screenX/20*13+izq.getLength())) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
                        spaceShip.setMovementState(spaceShip.UP);
                    }
                    else if ((motionEvent.getX() > (screenX/20*17))&&(motionEvent.getX() < (screenX/20*17+izq.getLength())) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
                        spaceShip.setMovementState(spaceShip.DOWN);
                    }
                    break;

                // Deja de pulsar la pantalla
                case MotionEvent.ACTION_UP:
                    spaceShip.setMovementState(spaceShip.STOPPED);
                    break;
            }
            if(spawnTimer == -1) {
                spawnTimer = System.currentTimeMillis();
            }
            return true;
        }
        if(lost) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                iniLvl();
            }
        }
        return true;
    }

    public void saveInfo(View view){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Ranking2", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i=1;i<=10;i++) {
            editor.putString("Rank "+i, "Empty-0");
            editor.apply();
        }
    }

    public void saveInfoR(View view,int score,String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Ranking2", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (name.compareTo("")==0){
            name="Anonymous";
        }
        int pos=findPos(view, score);
        if (pos!=-1){
            sortPreferences(view,pos);
            editor.putString("Rank "+pos,name+"-"+Integer.toString(score));
            editor.apply();

        }
    }

    public void sortPreferences(View view, int n){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Ranking2", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i=n;i<10;i++){
            editor.putString("Rank "+(n+1),sharedPreferences.getString("Rank "+n,"0"));
            editor.apply();
        }

    }
    public int findPos(View view,int score){
        int max=score;
        int pos=-1;
        SharedPreferences sharedPreferences = context.getSharedPreferences("Ranking2", Context.MODE_PRIVATE);
        for (int i=1;i<=10;i++){
            if (score>=Integer.parseInt(sharedPreferences.getString("Rank "+i,"0").split("-")[1])){
                max=Integer.parseInt(sharedPreferences.getString("Rank "+i,"0").split("-")[1]);
                return i;
            }
        }
        return pos;
    }

    public void display(View view){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Ranking2", Context.MODE_PRIVATE);

        System.out.println(sharedPreferences.getString("Rank 1","-1"));
        System.out.println(sharedPreferences.getString("Rank 2","-1"));
        System.out.println(sharedPreferences.getString("Rank 3","-1"));
    }

    public String getRank(InvadersGameView view,int i){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Ranking2", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Rank "+i,"-1");
    }

    private void drawR(InvadersGameView view){
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            paint.setColor(Color.argb(255, 249, 129, 0));
            Bitmap gameOver;
            Bitmap replay;
            Bitmap ranking;
            Bitmap home;

            gameOver = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover);
            replay = BitmapFactory.decodeResource(context.getResources(), R.drawable.replay);
            ranking = BitmapFactory.decodeResource(context.getResources(), R.drawable.trophy);
            home = BitmapFactory.decodeResource(context.getResources(), R.drawable.home);

            // Ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
            gameOver = Bitmap.createScaledBitmap(gameOver,
                    (int) (screenX/2),
                    (int) (screenY/2),
                    false);
            replay = Bitmap.createScaledBitmap(replay,
                    (int) (screenX/10),
                    (int) (screenY/10),
                    false);
            ranking = Bitmap.createScaledBitmap(ranking,
                    (int) (screenX/10),
                    (int) (screenY/10),
                    false);
            home = Bitmap.createScaledBitmap(home,
                    (int) (screenX/10),
                    (int) (screenY/10),
                    false);
            canvas.drawBitmap(gameOver, screenX/8*2.5f, screenY/8, paint);
            if(score >= 500) {
                canvas.drawBitmap(replay, screenX/8*4, screenY/8*6, paint);
            }
            canvas.drawBitmap(ranking, screenX/4*3, screenY/8*6, paint);
            canvas.drawBitmap(home, screenX/4, screenY/8*6, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }
}
