# Integrantes
- Darwin Lenis Maturana
- Dylan Bermudez
- Juan Felipe Madrid

# Problema del Monitor Dormilón

Este proyecto es una solución al problema del **Monitor Dormilón**, donde un monitor universitario atiende a estudiantes que buscan ayuda con sus tareas de programación. El problema consiste en modelar esta interacción utilizando programación concurrente con **hilos** y **semáforos** para controlar el acceso a los recursos compartidos.

## Descripción del Problema

En este escenario:
- El monitor está disponible para ayudar a los estudiantes, pero si no hay estudiantes, se queda dormido.
- Los estudiantes llegan en busca de ayuda. Si el monitor está atendiendo a otro estudiante, deben esperar en una fila de hasta 3 sillas.
- Si no hay sillas libres en el corredor, el estudiante se va a programar y vuelve más tarde.
- Cuando un estudiante despierta al monitor, este atiende a los estudiantes en orden de llegada.


