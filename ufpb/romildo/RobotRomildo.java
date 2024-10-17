package ufpb.romildo;
import robocode.*;
//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * RobotRomildo - a robot by Romildo
 */
public class RobotRomildo extends AdvancedRobot
{
	/**
	 * run: RobotRomildo's default behavior
	 */
    private boolean enemyDetected = false;  // Para rastrear se detectou um inimigo
    private double lastHitBearing = 0;      // Guarda a direção de onde foi atingido
    private boolean hitByBullet = false;    // Marca se foi atingido por uma bala

    @Override
    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        setTurnRadarRight(360);  // Inicia varredura do radar
        
        while (true) {
            if (!enemyDetected) {
                setAhead(100);  // Move-se para frente se não detectou inimigos
		setFire(1000);
                setTurnRadarRight(360);  // Escaneia periodicamente
            }
            if (hitByBullet) {
                fugir();  // Se for atingido, foge
                hitByBullet = false;  // Reseta flag após fugir
            }
            execute();  // Executa as ações do robô
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        enemyDetected = true;  // Marca que detectou um inimigo
        double absoluteBearing = getHeading() + event.getBearing();
        
        // Para e atira no inimigo
        setTurnRight(normalizeBearing(absoluteBearing - getGunHeading()));
        setTurnGunRight(normalizeBearing(absoluteBearing - getGunHeading()));
        setFire(200);  
        setAhead(0);  // Para o robô
        execute();
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        lastHitBearing = event.getBearing();  // Guarda a direção de onde foi atingido
        hitByBullet = true;  // Marca que foi atingido
    }

    private void fugir() {
        // Vira na direção oposta de onde foi atingido
        double oppositeDirection = normalizeBearing(lastHitBearing + 180);
        setTurnRight(oppositeDirection - getHeading());
        setAhead(300);  // Move-se rapidamente para longe
        setFire(200);  
    }

    // Método auxiliar para normalizar o ângulo entre -180 e 180
    private double normalizeBearing(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
	
	@Override
public void onHitWall(HitWallEvent event) {
    // Gera um número aleatório para decidir a direção do movimento e da rotação
    double randomDirection = Math.random();  // Número entre 0.0 e 1.0
    double randomMove = Math.random();  // Outro número aleatório para decidir avanço ou recuo

    // Se o valor for menor que 0.5, gira para a direita, senão gira para a esquerda
    if (randomDirection < 0.5) {
        setTurnRight(90);  // Gira 90 graus para a direita
    } else {
        setTurnLeft(90);  // Gira 90 graus para a esquerda
    }

    // Se o valor for menor que 0.5, anda para frente, senão anda para trás
    if (randomMove < 0.5) {
        setAhead(100);  // Move-se para frente 100 unidades
    } else {
        setBack(100);  // Move-se para trás 100 unidades
    }

    execute();  // Executa as ações programadas
}
}
