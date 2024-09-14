package src;
import java.util.concurrent.Semaphore;
import java.util.Random;

public class MonitorDormilon {
    // Número de sillas disponibles en la sala de espera
    private static int sillasLibres = 3; // Variable compartida (Zona Crítica)

    // Monitor que atiende a los estudiantes
    public static class Monitor extends Thread {
        private final Semaphore sillaDisponible; // Semáforo para controlar el acceso a la silla
        private boolean ocupado;           // Estado del monitor, si está ocupado o no
        private final Random random;       // Objeto random para simular tiempos aleatorios

        // Constructor de la clase. Inicializa todos los datos requeridos
        public Monitor(Semaphore sillaDisponible) {
            this.sillaDisponible = sillaDisponible;
            this.ocupado = false;
            this.random = new Random();
        }

        public void run() {
            while (true) {
                try {
                    // Espera a que un estudiante lo despierte
                    sillaDisponible.acquire(); // Adquiere el semáforo
                    
                    setOcupado(true);
                    Thread.sleep(random.nextInt(3000) + 1000); // Simula el tiempo que tarda en atender al estudiante
                    setOcupado(false);

                    incrementarSillasLibres(); // Libera una silla
                    
                    sillaDisponible.release(); // Libera el semáforo después de modificar sillasLibres
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private synchronized void incrementarSillasLibres() {
            sillasLibres++; // Operación sincronizada
        }

        public boolean isOcupado() {
            return ocupado;
        }

        public void setOcupado(boolean ocupado) {
            this.ocupado = ocupado;
        }
    }

    // Estudiantes que solicitan ayuda al monitor
    public static class Estudiante extends Thread {
        private final int idEstudiante;   // Número de estudiante
        private final Semaphore sillasAccesibles;  // Semáforo para controlar el acceso a las sillas disponibles
        private final Semaphore monitorDisponible; // Semáforo para indicar si el monitor está disponible
        private final Random random;              // Objeto random para simular tiempos aleatorios

        // Constructor del estudiante
        public Estudiante(int idEstudiante, Semaphore sillasAccesibles, Semaphore monitorDisponible) {
            this.idEstudiante = idEstudiante;
            this.sillasAccesibles = sillasAccesibles;
            this.monitorDisponible = monitorDisponible;
            this.random = new Random();
        }

        public void run() {
            while (true) {
                try {
                    sillasAccesibles.acquire(); // Controla el acceso a la zona crítica de las sillas

                    // Si hay sillas disponibles
                    if (haySillasDisponibles()) {
                        decrementarSillasLibres(); // El estudiante ocupa una silla
                        System.out.println("Estudiante " + idEstudiante + " se sienta en una silla del corredor.");
                        sillasAccesibles.release();

                        // Adquiere el semáforo para indicarle al monitor que puede atender
                        monitorDisponible.acquire(); 
                        System.out.println("Estudiante " + idEstudiante + " está siendo atendido por el monitor.");

                        // Simula el tiempo que tarda el monitor en atender
                        Thread.sleep(random.nextInt(1500) + 1000);

                        System.out.println("Estudiante " + idEstudiante + " ha terminado su consulta.");
                        monitorDisponible.release(); // Libera el semáforo para el siguiente estudiante
                        break;
                    } else {
                        // No hay sillas disponibles
                        sillasAccesibles.release();
                        System.out.println("No hay sillas libres. Estudiante " + idEstudiante + " se va a programar.");

                        // Simula el tiempo que tarda en programar antes de volver
                        Thread.sleep(random.nextInt(2000) + 2000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private synchronized boolean haySillasDisponibles() {
            return sillasLibres > 0; // Operación sincronizada
        }

        private synchronized void decrementarSillasLibres() {
            sillasLibres--; // Operación sincronizada
        }
    }

    public static void main(String[] args) {
    
        Semaphore sillaDisponible = new Semaphore(1); // Semáforo para controlar el acceso a la silla del monitor
        Semaphore sillasAccesibles = new Semaphore(1); // Semáforo para controlar el acceso a la zona crítica (sillas libres)
        Semaphore monitorDisponible = new Semaphore(1); // Semáforo para indicar si el monitor está disponible

        Monitor monitor = new Monitor(sillaDisponible); // Crear e iniciar el hilo del monitor
        monitor.start();

        // Crear e iniciar hilos de estudiantes
        for (int i = 0; i < 9; i++) {
            Estudiante estudiante = new Estudiante(i + 1, sillasAccesibles, monitorDisponible);
            estudiante.start();
            try {
                Thread.sleep(1000); // Espera un segundo antes de crear otro estudiante
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
