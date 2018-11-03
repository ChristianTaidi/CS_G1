package ps.spaceinvaders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class InvadersGameView extends SurfaceView implements Runnable {

    private boolean mode;

    Context context;

    private Thread mainTh;
    UpdateEnemiesThread enemiesThread;
    BulletManagerThread bulletThread;

    private SurfaceHolder holder;

    private volatile boolean isPlaying;

    private boolean isPaused;

    private Canvas canvas;
    private Paint paint;

    private int screenX;
    private int screenY;

    private long fps;
    private long timeFrame;

    //Elementos del juego
    private SpaceShip spaceShip;
    private Enemy[] enemies = new Enemy[60];
    private Defence[] blocks = new Defence[400];

    //Controlar las balas
    private ArrayList<Bullet> bullets = new ArrayList();
    private ArrayList<Bullet> removedBullets = new ArrayList();
    //Las naves no pueden diparar mas N balas por vez
    private boolean fullCapacity;
    private int enemyBulletsCount;
    private int maxEnemyBullets = 10;

    int numEnemies = 0;
    int killedEnemies = 0;
    private int numDefences;

    //Puntuacion
    int score = 0;
    boolean lost = false;

    private boolean animation = true;
    private long timeAnim = 1000;

    private long lastTime = System.currentTimeMillis();

    private boolean changeColor=false;

    //Botones de movimiento y disparo
    private Buttons izq,der,dis,arr,abj;

    public InvadersGameView (Context context, int x, int y, boolean isViolent){
        super(context);

        this.context = context;

        this.mode = isViolent;

        holder = getHolder();
        paint = new Paint();

        screenX= x;
        screenY= y;

        isPaused = true;

        iniLvl();
    }

    //THREADS QUE VAMOS A USAR

    //--------THREAD QUE INICIALIZA LA PARTIDA--------//
    class LoadingThread extends Thread {
        @Override
        public void run() {
            try {
                changeColor = false;
                killedEnemies = 0;
                bullets.clear();
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
                numEnemies = 0;
                for(int column = 0; column < 4; column ++ ){
                    for(int row = 0; row < 3; row ++ ){
                        enemies[numEnemies] = new Enemy(context, row, column, screenX, screenY);
                        numEnemies ++;
                    }
                }

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
                for (int i = 0; i < numEnemies; i++) {
                    if(RectF.intersects(spaceShip.getRect(), enemies[i].getRect())){
                        lost = true;
                    }
                    enemies[i].angryEnemie(killedEnemies);
                    if (enemies[i].getVisibility()) {
                        // Mueve enemy
                        enemies[i].update(fps);
                        checkAlienBlockCollision(enemies[i]);
                        // ¿Quiere hacer un disparo?
                        if (!fullCapacity && enemies[i].randomShot(spaceShip.getX(),
                                spaceShip.getLength(), killedEnemies)) {
                            Bullet b = new Bullet(context, screenY, screenX);
                            b.setEnemyBullet(true);
                            b.setFriend(true);
                            bullets.add(b);
                            enemyBulletsCount++;
                            if (bullets.get(bullets.size() - 1).shoot(enemies[i].getX()
                                            + enemies[i].getLength() / 2,
                                    enemies[i].getY(), bullets.get(bullets.size() - 1).DOWN)) {
                                if (enemyBulletsCount == maxEnemyBullets) {
                                    fullCapacity = true;
                                }
                            }
                        }
                        if (enemies[i].getX() > screenX - enemies[i].getLength() || enemies[i].getX() < 0){
                            bumped = true;
                        }
                    }
                }
                if(bumped){
                    // Mueve a todos los invaders hacia abajo y cambia la dirección
                    for(int i = 0; i < numEnemies; i++){
                        enemies[i].enemyCicle();
                        // Han llegado abajo
                        if((enemies[i].getY() > screenY - screenY / 10)&& enemies[i].isVisible){
                            lost = true;
                        }
                    }
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

    private void iniLvl(){
        spaceShip = new SpaceShip(context, screenX, screenY);
        izq=new Buttons(context,screenX,screenY,R.drawable.izq);
        der=new Buttons(context,screenX,screenY,R.drawable.der);
        dis=new Buttons(context,screenX,screenY,R.drawable.scope);
        arr=new Buttons(context,screenX,screenY,R.drawable.arr);
        abj=new Buttons(context,screenX,screenY,R.drawable.abj);
        LoadingThread load = new LoadingThread();
        load.start();
        enemiesThread = new UpdateEnemiesThread();
        bulletThread = new BulletManagerThread();
    }

    //Si la bala choca con los enemigos
    public void checkEnemyCollision(Bullet b) {
        for(int i = 0; i < numEnemies; i++) {
            if (enemies[i].getVisibility()) {
                if (!b.getFriend() && RectF.intersects(b.getRect(), enemies[i].getRect())) {
                    killedEnemies++;
                    enemies[i].setOff();
                    removedBullets.add(b);
                    score = score + 100;
                    checkVictory();
                }
            }
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
                if(RectF.intersects(b.getRect(), blocks[i].getRect())){
                    //b.setInactive();
                    blocks[i].destoyDefence();
                    removedBullets.add(b);
                    if(b.getEnemyBullet()) {
                        changeColor =!changeColor;
                    }
                }
            }
        }
    }

    public void checkVictory() {
        if(score == numEnemies * 100){
            lost = true;
        }
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
            draw();
            timeFrame = System.currentTimeMillis() - iniFrameTime;
            if (timeFrame >= 1) {
                fps = 1000 / timeFrame;
            }


        }
    }

    private void update(){

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

        if(lost){
            isPaused = true;
            iniLvl();
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
            canvas.drawBitmap(spaceShip.getBitmap(), spaceShip.getX(), spaceShip.getY(), paint);
            canvas.drawBitmap(izq.getBitmap(), 50, screenY - 200, paint);
            canvas.drawBitmap(der.getBitmap(), 500, screenY - 200, paint);
            canvas.drawBitmap(dis.getBitmap(), dis.getX()/2, screenY - 200, paint);
            canvas.drawBitmap(arr.getBitmap(), screenX-690, screenY - 200, paint);
            canvas.drawBitmap(abj.getBitmap(), screenX-240, screenY - 200, paint);

            // Dibuja las defensas no destruidas
            for(int i = 0; i < numDefences; i++){
                if(blocks[i].getActive()) {
                    canvas.drawRect(blocks[i].getRect(), paint);
                }
            }

            // Dibuja a los invaders
            for(int i = 0; i < numEnemies; i++) {
                if (enemies[i].getVisibility()) {
                    if (!changeColor) {
                        if (animation) {
                            canvas.drawBitmap(enemies[i].getBitmap(), enemies[i].getX(), enemies[i].getY(), paint);
                        } else {
                            canvas.drawBitmap(enemies[i].getBitmap2(), enemies[i].getX(), enemies[i].getY(), paint);
                        }
                    } else {
                        if (animation) {
                            canvas.drawBitmap(enemies[i].getBitmap3(), enemies[i].getX(), enemies[i].getY(), paint);
                        } else {
                            canvas.drawBitmap(enemies[i].getBitmap4(), enemies[i].getX(), enemies[i].getY(), paint);
                        }

                    }
                }
              }

            // Dibuja las balas de los invaders

            // Actualiza todas las balas de los invaders si están activas
            for(Bullet b : bullets) {
                if(!b.getEnemyBullet()){
                    canvas.drawBitmap(b.getBulletSpaceship(), b.getX()-b.getLength()/2, b.getY(), paint);
                }
                else {
                    canvas.drawBitmap(b.getBulletEnemy(), b.getX()-b.getLength()/2, b.getY(), paint);
                }
            }
            /*for(int i = 0; i < enemyShots.length; i++){
                if(enemyShots[i].getStatus()) {
                    canvas.drawRect(enemyShots[i].getRect(), paint);
                }
            }*/

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
                    if ((motionEvent.getX() < (screenX / 5)) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
                        spaceShip.setMovementState(spaceShip.LEFT); }
                    else if ((motionEvent.getX() < (screenX / 5*2)) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
                        spaceShip.setMovementState(spaceShip.RIGHT);
                    } else if ((motionEvent.getX() < (screenX / 5*3)) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
                        Bullet b = new Bullet(context, screenY, screenX);
                        bullets.add(b);
                        b.shoot(spaceShip.getX() + spaceShip.getLength() / 2, spaceShip.getY()-spaceShip.getHeight(), b.UP);
                    } else if ((motionEvent.getX() < (screenX / 5*4)) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
                        spaceShip.setMovementState(spaceShip.UP);
                    } else if ((motionEvent.getX() < ( (screenX ))) && (motionEvent.getY() > (screenY - (screenY / 6)))) {
                        spaceShip.setMovementState(spaceShip.DOWN);
                    }
                    break;

                // Deja de pulsar la pantalla
                case MotionEvent.ACTION_UP:
                    spaceShip.setMovementState(spaceShip.STOPPED);
                    break;
            }
            return true;
        }
        return true;
    }


}
