package cs.spaceinvaders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cs.spaceinvaders.entity.EntityImages;
import cs.spaceinvaders.entity.SpecialEnemy;
import cs.spaceinvaders.entity.Bullet;
import cs.spaceinvaders.entity.Buttons;
import cs.spaceinvaders.entity.Defence;
import cs.spaceinvaders.entity.Enemy;
import cs.spaceinvaders.entity.SpaceShip;

public class InvadersGameView extends SurfaceView implements Runnable {

    private boolean mode;

    private MediaPlayer mp;

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
    private long timer;
    private long spawnTimer;
    private  int increments = 1;

    //Elementos del juego
    private ArrayList<Enemy> enemiesList = new ArrayList();
    private ArrayList<Enemy> spawnedEnemies = new ArrayList();
    private SpaceShip spaceShip;
    private SpecialEnemy specialEnemy;
    private Defence[] blocks = new Defence[400];

    //Controlar las balas
    private List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private List<Bullet> removedBullets = new CopyOnWriteArrayList<>();
    //Las naves no pueden diparar mas N balas por vez
    private boolean fullCapacity;
    private int enemyBulletsCount;
    private int maxEnemyBullets = 10;

    int totalEnemies = 0;
    int killedEnemies = 0;
    private int numDefences;
    private int spawnCount;
    private Enemy lastSpawned;
    private long jumpTimer;

    //Puntuacion
    int score = 0;
    int bonus = 0;
    boolean lost = false;
    boolean win = false;

    boolean isReloading = false;

    private boolean animation = true;
    private boolean invulnerableAnimation = true;
    private long timeAnim = 1000;

    private long lastTime = System.currentTimeMillis();
    private long lastIvulnerableAnimation = System.currentTimeMillis();
    private long lastSpecialSpawned;
    private long lastTp;
    private long lastInvulnerable;

    private boolean changeColor=false;

    private Bitmap bulletBitmap;
    private Bitmap specialBulletBitmap;
    private Bitmap enemyAnim1Bitmap;
    private Bitmap enemyAnim2Bitmap;
    private Bitmap enemyAnim3Bitmap;
    private Bitmap enemyAnim4Bitmap;
    private Bitmap spaceshipBitmap;
    private Bitmap spaceshipInvulnerable;
    private Bitmap gameOver;
    private Bitmap gameWon;
    private Bitmap specialEnemyBitMap;
    private Bitmap avatarEmpty;
    private Bitmap background;
    private Bitmap rock;


    //Botones de movimiento y disparo
    private Buttons izq;
    private Buttons der;
    private Buttons dis;
    private Buttons arr;
    private Buttons abj;
    private Buttons home;
    private Buttons ranking;
    private Buttons restart;

    //Elementos a guardar en el shared Preferences que llegan desde mainActivity
    private String name;
    private String proFilePicEncoded;

    //Contador
    private int count = 0;

    private static final String RANKING2 = "RANKING2";
    private static final String RANK = "RANK";
    private static final String PHOTO = "Photo";

    private EntityImages images;

    public InvadersGameView (Context context, int x, int y, boolean isViolent,String name, String profilePicEncoded){
        super(context);
        mp = MediaPlayer.create(context,R.raw.sound);
        mp.setLooping(true);
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.background), x, y, false);
        specialEnemyBitMap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ufo), x/6, y/8, false);
        spaceshipInvulnerable = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceshipinvulnerable), x/8, y/15, false);
        spaceshipBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship), x/8, y/15, false);
        bulletBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet1), x/20, y/20, false);
        specialBulletBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet1), x/10, y/10, false);
        enemyAnim1Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderstart), x/20, y/20, false);
        enemyAnim2Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderend), x/20, y/20, false);
        enemyAnim3Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderstart2), x/20, y/20, false);
        enemyAnim4Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderend2), x/20, y/20, false);

        images=new EntityImages(spaceshipBitmap,enemyAnim1Bitmap,enemyAnim2Bitmap,enemyAnim3Bitmap,enemyAnim4Bitmap,bulletBitmap);

        avatarEmpty = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.avatarvacio));


        rock = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.rock), x/20, y/80, false);

        izq=new Buttons(context,x,y,R.drawable.izq, x/20*1, y-400);
        der=new Buttons(context,x,y,R.drawable.der, x/20*5, y-400);
        dis=new Buttons(context,x,y,R.drawable.scope,x/20*9, y-400);
        arr=new Buttons(context,x,y,R.drawable.arr,x/20*12, y-400);
        abj=new Buttons(context,x,y,R.drawable.abj,x/20*17, y-400);

        gameOver = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover), x/2, y/2, false);
        gameWon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.win), x/2, y/2, false);

        //This buttons use a different constructor so its easier to access to their X and Y variables
        restart = new Buttons(context,x,y,R.drawable.replay, x/8*4-100, y/8*6);
        home = new Buttons(context,x,y,R.drawable.home, x/8, y/8*6);
        ranking = new Buttons(context,x,y,R.drawable.trophy, x/8*7-200, y/8*6);



        this.context = context;
        this.name=name;
        this.proFilePicEncoded = profilePicEncoded;

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
                win = false;
                jumpTimer = -1;
                lastSpawned = new Enemy(context, 0, 0, screenX, screenY, enemyAnim1Bitmap, enemyAnim2Bitmap, enemyAnim3Bitmap, enemyAnim4Bitmap);
                spawnCount = 0;
                increments = 1;
                isReloading = false;
                spaceShip = new SpaceShip(context, screenX, screenY, spaceshipBitmap);
                specialEnemy = new SpecialEnemy(context,screenX, screenY, specialEnemyBitMap);
                spaceShip.resetShootsCount();
                changeColor = false;
                killedEnemies = 0;
                bullets.clear();
                enemiesList.clear();
                spawnedEnemies.clear();
                score = 0;
                bonus = 0;
                fullCapacity = false;
                enemyBulletsCount = 0;

                // Construye las defensas
                numDefences= 0;
                for(int shelterNumber = 0; shelterNumber < 3; shelterNumber++){
                    for(int column = 1; column < 12; column ++ ) {
                        for (int row = 0; row < 7; row++) {
                            if (!((row < 4 && column < 5 && row + column < 5)||
                                    (row < 4 && column > 7 && column-row > 7)||
                                    (row>=4 && column >=5 && column <=7)||
                                    (row>=5 && column == 4)||
                                    (row>=5 && column == 8))) {
                                blocks[numDefences] = new Defence(row, column, shelterNumber, screenX, screenY);
                                numDefences++;
                            }

                        }
                    }
                }

                // Construye la formación enemiga
                for(int column = 2; column < 10; column ++ ){
                    for(int row = 3; row <= 5; row ++ ){
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
                    if(spaceShip.isVulnerable() && RectF.intersects(spaceShip.getRect(), enemiesList.get(i).getRectF())) {
                        lost = true;

                    }
                        // Mueve enemy
                        enemiesList.get(i).update(fps);
                        checkAlienBlockCollision(enemiesList.get(i));
                        // ¿Quiere hacer un disparo?
                        //Is Violent?
                        if(mode) {
                            if (!fullCapacity && enemiesList.get(i).randomShot(spaceShip.getX(),
                                    spaceShip.getLength())) {
                                bulletBitmap = Bitmap.createScaledBitmap(bulletBitmap, screenX / 20, screenY / 20, false);
                                Bullet b = new Bullet( screenY, screenX, bulletBitmap);
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
                if(specialEnemy.isSpawned()) {
                    specialEnemy.update(fps);
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


                    if(specialEnemy.isSpawned()) {
                        if(b.hasCollided(specialEnemy.getRect())) {
                            removedBullets.add(b);
                            bonus +=500;
                            score+=500;
                            specialEnemy.setSpawned(false);
                            continue;
                        }
                    }

                    b.update(fps);

                    //Comprueba limites pantalla
                    if (b.getImpactPointY() < 0 || b.getImpactPointY() > screenY-80) {
                        b.changeDirection();
                        b.updateBounceCounts();
                        //Una bala solo puede rebotar 2 veces
                        if (b.getBounceCounts() == 2) {
                            removedBullets.add(b);
                            continue;

                        }
                        b.setFriend(false);
                    }

                    //Si la bala choca con los enemigos
                    checkEnemyCollision(b);

                    //Si la bala choca con los bloques
                    checkBlockBulletCollision(b);

                    //Si la bala choca con el jugador

                    if(spaceShip.isVulnerable()) {
                        if (spaceShip.hasCollided( spaceShip.getRect())) {
                            lost = true;
                        }
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
                if (!b.getFriend() && b.hasCollided(enemiesList.get(i).getRectF())) {
                    if(enemiesList.get(i).isSpawned()) {
                        spawnedEnemies.remove(enemiesList.get(i));
                    }
                    enemiesList.remove(enemiesList.get(i));
                    removedBullets.add(b);
                    score = score + 100;
                    killedEnemies++;
                    checkVictory();
                }
        }
    }

    public void checkAlienBlockCollision(Enemy e) {
        for(int i = 0; i < numDefences; i++) {

            if(blocks[i].getActive()) {
                if(RectF.intersects(blocks[i].getRectF(), e.getRectF())) {
                    blocks[i].destoyDefence();
                }
            }
        }
    }

    public void checkPlayerBlockCollision(){
        if(spaceShip.isVulnerable()) {
            for (int i = 0; i < numDefences; i++) {
                if (blocks[i].getActive()) {
                    RectF r = blocks[i].getRectF();
                    if (RectF.intersects(r, spaceShip.getRect())) {
                        lost = true;
                    }
                }
            }
        }
    }

    public void checkBlockBulletCollision(Bullet b){
        for(int i = 0; i < numDefences; i++){
            if(blocks[i].getActive()){

                RectF r = blocks[i].getRectF();
                if(b.hasCollided(r)){
                    //b.setInactive();
                    blocks[i].destoyDefence();
                    removedBullets.add(b);
                    if(b.getEnemyBullet()) {
                        images.changeEnemyColor();
                    }
                }
            }
        }
    }

    public void checkVictory() {
        if(score-bonus == totalEnemies * 100){
            lost = true;
            win = true;
        }
    }

    public void playerShoot() {
        //Is Violent
        if(mode) {
            Bullet b = new Bullet( screenY, screenX, bulletBitmap);
            bullets.add(b);
            b.shoot(spaceShip.getX() + spaceShip.getLength() / 2, spaceShip.getY()
                    - spaceShip.getHeight()-100, b.UP);
        }
    }

    public void playerSpecialShoot(){
        if (mode) {
            Bullet special = new Bullet( screenY, screenX, specialBulletBitmap);
            bullets.add(special);
            special.shoot(spaceShip.getX() + spaceShip.getLength() / 2, spaceShip.getY()
                    - spaceShip.getHeight() - 100, special.UP);
        }
    }

    @Override
    public void run() {
        while(isPlaying) {
            long iniFrameTime = System.currentTimeMillis();
            if (!isPaused) {
                update();

                if((iniFrameTime - lastSpecialSpawned) > 10000) {
                    lastSpecialSpawned = System.currentTimeMillis();
                    specialEnemy = new SpecialEnemy(context,screenX, screenY, specialEnemyBitMap);
                    specialEnemy.setSpawned(true);
                }

                if(spaceShip.getTpTime() == -1){
                    spaceShip.setRandomTp();
                }

                if((iniFrameTime - lastTp) > spaceShip.getTpTime()*1000){
                    lastTp = System.currentTimeMillis();
                    spaceShip.teleport();
                    spaceShip.setVulnerable(false);
                    spaceShip.setTpTime(-1);
                    images.setShipImage(spaceshipInvulnerable);
                    lastInvulnerable = System.currentTimeMillis();
                }
                if(!spaceShip.isVulnerable()){
                    if((iniFrameTime - lastInvulnerable) > 2000){
                        lastInvulnerable = System.currentTimeMillis();
                        spaceShip.setVulnerable(true);
                        images.setShipImage(spaceshipBitmap);
                    }
                }

                if((iniFrameTime - lastTime) > timeAnim){
                    lastTime = System.currentTimeMillis();

                    images.animate();
                }

                if((iniFrameTime - lastIvulnerableAnimation) > 200){
                    lastIvulnerableAnimation = System.currentTimeMillis();

                    invulnerableAnimation = !invulnerableAnimation;
                }


            }
            if(!lost){
                draw();
            }
            else {
                mp.stop();
                try {
                    mp.prepare();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                drawR(this);
            }

            long timeFrame;

            timeFrame = System.currentTimeMillis() - iniFrameTime;
            if (timeFrame >= 1) {
                fps = 1000 / timeFrame;
            }
        }
    }

    private void update(){
        if (!mp.isPlaying()){
            mp.start();
        }
        checkPlayerBlockCollision();
        if(spaceShip.isVulnerable()) {
            if (RectF.intersects(spaceShip.getRect(), specialEnemy.getRect())) {
                lost = true;
            }
        }
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

        if(System.currentTimeMillis() >= timer+600){
            isReloading = false;
            spaceShip.resetShootsCount();
        }

        if(lost){
            saveInfoR(this,score,name,proFilePicEncoded);
            isPaused = true;
        }
    }


    private void draw(){
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawBitmap(background, 0,0, paint);

            //Pintar la puntuación
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(50);
            canvas.drawText("Score: " + score, 30,50, paint);

            // Dibuja la nave espacial

            canvas.drawBitmap(izq.getBitmap(), izq.getX(), izq.getY(), paint);
            canvas.drawBitmap(der.getBitmap(), der.getX(), der.getY(), paint);
            if(mode) {
                canvas.drawBitmap(dis.getBitmap(), dis.getX(), dis.getY(), paint);
            }
            canvas.drawBitmap(arr.getBitmap(), arr.getX(), arr.getY(), paint);
            canvas.drawBitmap(abj.getBitmap(), abj.getX(), abj.getY(), paint);
            if(!spaceShip.isVulnerable() && invulnerableAnimation){
                canvas.drawBitmap(images.getShipImage(), spaceShip.getX(), spaceShip.getY(), paint);
            }
            else if(spaceShip.isVulnerable()){
                canvas.drawBitmap(images.getShipImage(), spaceShip.getX(), spaceShip.getY(), paint);
            }
            if(specialEnemy.isSpawned()) {
                canvas.drawBitmap(specialEnemy.getBitmap(), specialEnemy.getX(), specialEnemy.getY(), paint);
            }
            // Dibuja las defensas no destruidas
            for(int i = 0; i < numDefences; i++){
                if(blocks[i].getActive()) {

                    //canvas.drawRect(blocks[i].getRect(), paint);
                    canvas.drawBitmap(rock,blocks[i].getRectF().left,blocks[i].getRectF().top,paint);
                }
            }

            // Dibuja a los invaders
            for(int i = 0; i < enemiesList.size(); i++) {

                canvas.drawBitmap(images.getEnemyImage(), enemiesList.get(i).getX(), enemiesList.get(i).getY(), paint);


            }

            // Dibuja las balas de los invaders
            if(!bullets.isEmpty() && !bulletThread.isAlive()) {
                for (Bullet b : bullets) {
                    if (!b.getEnemyBullet()) {
                        canvas.drawBitmap(images.getEnemyImage(), b.getX() - b.getHeight() / 2, b.getY(), paint);
                    } else {
                        canvas.drawBitmap(images.getBulletImage(), b.getX() - b.getHeight() / 2, b.getY(), paint);
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
                   if (isPaused){
                       lastSpecialSpawned = System.currentTimeMillis();
                       lastTp = System.currentTimeMillis();
                       lastInvulnerable = System.currentTimeMillis();
                   }
                    isPaused = false;
                    if( motionEvent.getX() >= izq.getX() && motionEvent.getX() <
                            (izq.getX() + izq.getLength()) &&
                            motionEvent.getY() >= izq.getY() && motionEvent.getY() <
                            (izq.getY() + izq.getHeight())) {
                        spaceShip.setMovementState(spaceShip.LEFT);
                    }
                    else if ( motionEvent.getX() >= der.getX() && motionEvent.getX() <
                            (der.getX() + der.getLength()) &&
                            motionEvent.getY() >= der.getY() && motionEvent.getY() <
                            (der.getY() + der.getHeight())) {
                        spaceShip.setMovementState(spaceShip.RIGHT);
                    }
                    if ( motionEvent.getX() >= dis.getX() && motionEvent.getX() <
                            (dis.getX() + dis.getLength()) &&
                            motionEvent.getY() >= dis.getY() && motionEvent.getY() <
                            (dis.getY() + dis.getHeight())) {
                        if(!isReloading) {
                            count++;
                            if (count < 5){
                                playerShoot();
                            } else {
                                playerSpecialShoot();
                                count = 0;
                            }
                            spaceShip.addShootsCount();
                            timer = System.currentTimeMillis();
                            if(spaceShip.getShootsCount() >= 1) {
                                isReloading = true;
                            }
                        }

                    }
                    else if ( motionEvent.getX() >= arr.getX() && motionEvent.getX() <
                            (arr.getX() + arr.getLength()) &&
                            motionEvent.getY() >= arr.getY() && motionEvent.getY() <
                            (arr.getY() + arr.getHeight())) {
                        spaceShip.setMovementState(spaceShip.UP);
                    }
                    else if ( motionEvent.getX() >= abj.getX() && motionEvent.getX() <
                            (abj.getX() + abj.getLength()) &&
                            motionEvent.getY() >= abj.getY() && motionEvent.getY() <
                            (abj.getY() + abj.getHeight())) {
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
                //Button Replay
                if(score >= 500 && motionEvent.getX() >= restart.getX() && motionEvent.getX() <
                        (restart.getX() + restart.getLength()) &&
                        motionEvent.getY() >= restart.getY() && motionEvent.getY() <
                        (restart.getY() + restart.getHeight())) {
                    iniLvl();
                }
                //Button Home
                else if( motionEvent.getX() >= home.getX() && motionEvent.getX() <
                        (home.getX() + home.getLength()) &&
                        motionEvent.getY() >= home.getY() && motionEvent.getY() <
                        (home.getY() + home.getHeight())) {
                    Intent intentMain = new Intent(context,MainActivity.class);
                    context.startActivity(intentMain);
                }
                //Button RANKING2
                else if( motionEvent.getX() >= ranking.getX() && motionEvent.getX() <
                        (ranking.getX() + ranking.getLength()) &&
                        motionEvent.getY() >= ranking.getY() && motionEvent.getY() <
                        (ranking.getY() + ranking.getHeight())) {
                    Intent intentRanking = new Intent (context,RankingActivity.class);
                    context.startActivity(intentRanking);
                }
            }
        }
        return true;
    }
    public void saveInfo(View view){
        ImageEncoder encoder = new ImageEncoder(avatarEmpty);
        String photoDefault = encoder.getEncodedImage();
        SharedPreferences sharedPreferences = context.getSharedPreferences(RANKING2, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i=1;i<=10;i++) {
            editor.putString(RANK +i, "Empty-0");
            editor.putString(PHOTO +i,  photoDefault);
            editor.apply();
        }
    }

    public int findPos(View view,int score){
        int pos=-1;
        SharedPreferences sharedPreferences = context.getSharedPreferences(RANKING2, Context.MODE_PRIVATE);
        for (int i=1;i<=10;i++){
            if (score>=Integer.parseInt(sharedPreferences.getString(RANK +i,"0").split("-")[1])){
                return i;
            }
        }
        return pos;
    }

    public void saveInfoR(View view,int score,String name, String proFilePicEncoded){
        SharedPreferences sharedPreferences = context.getSharedPreferences(RANKING2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (name.compareTo("")==0){
            name="Anonymous";
        }
        int pos=findPos(view, score);
        if (pos!=-1){
            sortPreferences(view,pos);
            editor.putString(RANK +pos,name+"-"+Integer.toString(score));
            editor.putString(PHOTO +pos,proFilePicEncoded);
            editor.apply();
        }
    }

    public void sortPreferences(View view, int n){
        SharedPreferences sharedPreferences = context.getSharedPreferences(RANKING2, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i=n;i<10;i++){
            editor.putString(RANK +(n+1),sharedPreferences.getString(RANK +n,"0"));
            editor.putString(PHOTO +(n+1),sharedPreferences.getString(PHOTO +n,"0"));
            editor.apply();
        }
    }


    public String getRank(InvadersGameView view,int i){
        SharedPreferences sharedPreferences = context.getSharedPreferences(RANKING2, Context.MODE_PRIVATE);
        return sharedPreferences.getString("RANK "+i,"-1");
    }
    private void drawR(InvadersGameView view){
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            paint.setColor(Color.argb(255, 249, 129, 0));

            // Ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
            if(win){
                canvas.drawBitmap(gameWon, screenX/8*2, screenY/8, paint);
            }
            else {
                canvas.drawBitmap(gameOver, screenX/8*2, screenY/8, paint);
            }
            if(score >= 500) {
                canvas.drawBitmap(this.restart.getBitmap(), restart.getX(), restart.getY(), paint);
            }
            canvas.drawBitmap(this.ranking.getBitmap(), ranking.getX(), ranking.getY(), paint);
            canvas.drawBitmap(this.home.getBitmap(), home.getX(), home.getY(), paint);

            holder.unlockCanvasAndPost(canvas);
        }

    }

}
