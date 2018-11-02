package ps.spaceinvaders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class InvadersGameView extends SurfaceView implements Runnable {

    private boolean mode;

    Context context;

    private Thread mainTh;

    private SurfaceHolder holder;

    private volatile boolean isPlaying;

    private boolean isPaused = false;

    private Canvas canvas;
    private Paint paint;

    private int screenX;
    private int screenY;

    private long fps;
    private long timeFrame;

    //Elementos del juego
    private SpaceShip spaceShip;
    private Bullet bullet;
    private Enemy[] enemies = new Enemy[60];
    private Defence[] blocks = new Defence[400];

    //Balas invaders
    private Bullet[] enemyShots = new Bullet[200];
    private int nextBullet;
    private int maxEnemyBullets = 10;

    int numEnemies = 0;
    private int numDefences;

    int score = 0;

    private boolean animation = true;

    private long timeAnim = 1000;
    private long lastTime = System.currentTimeMillis();

    boolean lost = false;
    private boolean changeColor=false;

    private Buttons izq,der,dis,arr,abj;

    public InvadersGameView (Context context, int x, int y, boolean isViolent){
        super(context);

        this.context = context;

        this.mode = isViolent;

        holder = getHolder();
        paint = new Paint();

        screenX= x;
        screenY= y;

        iniLvl();
    }

    private void iniLvl(){
        spaceShip = new SpaceShip(context, screenX, screenY);
        izq=new Buttons(context,screenX,screenY,R.drawable.izq);
        der=new Buttons(context,screenX,screenY,R.drawable.der);
        dis=new Buttons(context,screenX,screenY,R.drawable.shoot);
        arr=new Buttons(context,screenX,screenY,R.drawable.arr);
        abj=new Buttons(context,screenX,screenY,R.drawable.abj);

        // Prepara la bala del jugador
        bullet = new Bullet(screenY);

        // Inicializa la formación de invadersBullets
        for(int i = 0; i < enemyShots.length; i++) {
            enemyShots[i] = new Bullet(screenY);
        }

        // Construye la formación enemiga
        numEnemies = 0;
        for(int column = 0; column < 4; column ++ ){
            for(int row = 0; row < 3; row ++ ){
                enemies[numEnemies] = new Enemy(context, row, column, screenX, screenY);
                numEnemies ++;
            }
        }

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

        lost=false;
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
        boolean bumped = false;

        // Mueve la nave espacial
        spaceShip.update(fps);

        // Actualiza todos los enemies activos
        for(int i = 0; i < numEnemies; i++){

            if(enemies[i].getVisibility()) {
                // Mueve enemy
                enemies[i].update(fps);

                // ¿Quiere hacer un disparo?
                if (enemies[i].randomShot(spaceShip.getX(),
                        spaceShip.getLength())) {

                    if (enemyShots[nextBullet].shoot(enemies[i].getX()
                                    + enemies[i].getLength() / 2,
                            enemies[i].getY(), bullet.DOWN)) {

                        // Disparo realizado
                        nextBullet++;

                        if (nextBullet == maxEnemyBullets) {
                            //Espera a que una bala acabe su trayecto para tener disponible la siguiente
                            nextBullet = 0;
                        }
                    }
                }

                // cambia bumped a true si toca laterales de pantalla
                if (enemies[i].getX() > screenX - enemies[i].getLength() || enemies[i].getX() < 0){
                    bumped = true;
                }
            }
        }

        // Actualiza todas las balas de los enemigos activas
        for(int i = 0; i < enemyShots.length; i++){
            if(enemyShots[i].getStatus()) {
                enemyShots[i].update(fps);
            }
        }

        // Si toca el lateral de la pantalla
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

        if(mode){
            // Actualiza la bala del jugador
            if(bullet.getStatus()) {
                bullet.update(fps);
            }
        }

        // Comprueba si ha llegado al final la bala del jugador
        if(bullet.getImpactPointY() < 0){
            bullet.setInactive();
        }

        // Comprueba si ha llegado al final la bala del enemigo
        for(int i = 0; i < enemyShots.length; i++){
            if(enemyShots[i].getImpactPointY() > screenY){
                enemyShots[i].setInactive();
            }
        }

        // Comprueba si ha tocado la bala del jugador a algún invader
        if(bullet.getStatus()) {
            for (int i = 0; i < numEnemies; i++) {
                if (enemies[i].getVisibility()) {
                    if (RectF.intersects(bullet.getRect(), enemies[i].getRect())) {
                        enemies[i].setOff();
                        //soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        bullet.setInactive();
                        score = score + 100;

                        // Ha ganado el jugador
                        if(score == numEnemies * 100){
                            lost = true;
                        }
                    }
                }
            }
        }

        // Comprueba si chocado la bala enemiga con la defensa
        for(int i = 0; i < enemyShots.length; i++){
            if(enemyShots[i].getStatus()){
                for(int j = 0; j < numDefences; j++){
                    if(blocks[j].getActive()){
                        if(RectF.intersects(enemyShots[i].getRect(), blocks[j].getRect())){
                            //Ha ocurrido la colisión
                            enemyShots[i].setInactive();
                            blocks[j].destoyDefence();
                            changeColor =!changeColor;

                        }
                    }
                }
            }
        }

        // Comprueba si ha chocado la bala del jugador con las defensas
        if(bullet.getStatus()){
            for(int i = 0; i < numDefences; i++){
                if(blocks[i].getActive()){
                    if(RectF.intersects(bullet.getRect(), blocks[i].getRect())){
                        bullet.setInactive();
                        blocks[i].destoyDefence();
                        //soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                    }
                }
            }
        }

        // Comprueba si un disparo enemigo nos ha tocado
        for(int i = 0; i < enemyShots.length; i++){
            if(enemyShots[i].getStatus()){
                if(RectF.intersects(spaceShip.getRect(), enemyShots[i].getRect())){
                    enemyShots[i].setInactive();
                    //soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);
                    lost = true;
                }
            }
        }

        if(lost){
            isPaused = true;
            score = 0;
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

            // Dibuja la bala del jugador
            if(bullet.getStatus()){
                paint.setColor(Color.argb(255, 255, 255, 0));
                canvas.drawRect(bullet.getRect(), paint);
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
            for(int i = 0; i < enemyShots.length; i++){
                if(enemyShots[i].getStatus()) {
                    canvas.drawRect(enemyShots[i].getRect(), paint);
                }
            }

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
                        bullet.shoot(spaceShip.getX() + spaceShip.getLength() / 2, spaceShip.getY(), bullet.UP);
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
