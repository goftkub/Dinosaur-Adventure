package gameComponents;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import zimgHandlers.Animator;
import zimgHandlers.ImgCompiler;

public class Player {

    public static final int LAND_Y_POS = 80;
    public static final float GRAVITY = 0.4f;

    private static final int NORMAL_RUN = 0;
    private static final int JUMP = 1;
    private static final int CROUCH = 2;
    private static final int DEAD = 3;

    private float positionY;
    private float positionX;
    private float speedX;
    private float speedY;
    private Rectangle hitbox;
    
    public int score = 0;

    private int state = NORMAL_RUN;

    private Animator runAnimation;
    private BufferedImage jumpAnimation;
    private Animator crouchAnimation;
    private BufferedImage deathStillPic;

    private AudioClip jSound;
    private AudioClip dSound;
    private AudioClip scoreHundredSound;

    public Player() {
        positionX = 50;
        positionY = LAND_Y_POS;
        hitbox = new Rectangle();
        runAnimation = new Animator(90);
        runAnimation.addFrame(ImgCompiler.getResouceImage("data/main-character1.png"));
        runAnimation.addFrame(ImgCompiler.getResouceImage("data/main-character2.png"));
        jumpAnimation = ImgCompiler.getResouceImage("data/main-character3.png");
        crouchAnimation = new Animator(90);
        crouchAnimation.addFrame(ImgCompiler.getResouceImage("data/main-character5.png"));
        crouchAnimation.addFrame(ImgCompiler.getResouceImage("data/main-character6.png"));
        deathStillPic = ImgCompiler.getResouceImage("data/main-character4.png");

        try {
            jSound =  Applet.newAudioClip(new URL("file","","data/jump.wav"));
            dSound =  Applet.newAudioClip(new URL("file","","data/dead.wav"));
            scoreHundredSound =  Applet.newAudioClip(new URL("file","","data/scoreup.wav"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void drawPlayerComponents(Graphics g) {
        switch(state) {
            case NORMAL_RUN:
                g.drawImage(runAnimation.getFrame(), (int) positionX, (int) positionY, null);
                break;
            case JUMP:
                g.drawImage(jumpAnimation, (int) positionX, (int) positionY, null);
                break;
            case CROUCH:
                g.drawImage(crouchAnimation.getFrame(), (int) positionX, (int) (positionY + 20), null);
                break;
            case DEAD:
                g.drawImage(deathStillPic, (int) positionX, (int) positionY, null);
                break;
        }
    }

    public void updatePlayerState() {
        runAnimation.updateFrame();
        crouchAnimation.updateFrame();
        if(positionY >= LAND_Y_POS) {
            positionY = LAND_Y_POS;
            if(state != CROUCH) {
                state = NORMAL_RUN;
            }
        } else {
            speedY += GRAVITY;
            positionY += speedY;
        }
    }

    public void jumpArcMaker() {
        if(positionY >= LAND_Y_POS) {
            if(jSound != null) {
                jSound.play();
            }
            speedY = -7.5f;
            positionY += speedY;
            state = JUMP;
        }
    }

    public void crouchPersist(boolean isDown) {
        if(state == JUMP) {
            return;
        }
        if(isDown) {
            state = CROUCH;
        } else {
            state = NORMAL_RUN;
        }
    }

    public Rectangle getHitBox() {
        hitbox = new Rectangle();
        if(state == CROUCH) {
            hitbox.x = (int) positionX + 5;
            hitbox.y = (int) positionY + 20;
            hitbox.width = crouchAnimation.getFrame().getWidth() - 10;
            hitbox.height = crouchAnimation.getFrame().getHeight();
        } else {//get bounds of run image
            hitbox.x = (int) positionX + 5;
            hitbox.y = (int) positionY;
            hitbox.width = runAnimation.getFrame().getWidth() - 10;
            hitbox.height = runAnimation.getFrame().getHeight();
        }
        return hitbox;
    }

    public void playerDeadState(boolean isDeath) {
        if(isDeath) {
            state = DEAD;
        } else {
            state = NORMAL_RUN;
        }
    }

    public void returnToLand() {
        positionY = LAND_Y_POS;
    }

    public void playDeadOof() {
        dSound.play();
    }

    public void incrementScore() {
        score += 20;
        if(score % 100 == 0) {
            scoreHundredSound.play();
        }
    }
        	
}