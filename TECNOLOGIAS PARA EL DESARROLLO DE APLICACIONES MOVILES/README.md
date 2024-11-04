1. Averigua las características hardware de tu dispositivo móvil. ¿Qué
procesador emplea? ¿Cuenta con GPU? Memoria RAM, tipo de
pantalla…

Tiene un procesador Mediatek Dimensity 8100-Ultra, Mali-G610 MC6, con 8GB de RAM y una pantalla AMILED de 6.67 pulgadas.

## 2. Versiones de ARM
- **Familia ARMv**: Cada versión incrementa en capacidad, eficiencia y soporte de instrucciones avanzadas.
- **ARMv1**: La primera versión, lanzada en 1985. Se utilizó en el primer chip ARM, el ARM1.
- **ARMv2**: Introdujo instrucciones adicionales y mejoras.
- **ARMv3**: Lanzado en 1991, este fue el primer ARM que soportaba una dirección de 32 bits.
- **ARMv4**: Introdujo mejoras generales y se incluyó una versión con soporte para una arquitectura de 64 bits (ARMv4T).
- **ARMv5**: Añadió instrucciones DSP y mejoras en el rendimiento.
- **ARMv6**: Lanzado en 2002, introdujo características como el soporte para SIMD (Single Instruction, Multiple Data) y la arquitectura ARM1176JZF-S que se usó en dispositivos como el iPhone original.
- **ARMv7**: Esta es una de las arquitecturas ARM más populares y versátiles, lanzada en 2005. ARMv7-A se utiliza en muchos smartphones y tablets. ARMv7-M está diseñada para microcontroladores, y ARMv7-R para sistemas en tiempo real.
- **ARMv8-A**: Introducido en 2011, este es un gran cambio para ARM, ya que añade soporte para 64 bits. Está dividido en dos conjuntos de instrucciones, AArch64 para 64 bits y AArch32 para 32 bits. Esta arquitectura se utiliza en muchos de los dispositivos modernos, incluyendo iPhones, iPads y muchos dispositivos Android de gama alta.
- **ARMv8.1-A, ARMv8.2-A, ARMv8.3-A y ARMv8.4-A**: Son extensiones y mejoras del ARMv8-A, que añaden características y optimizaciones adicionales.
- **ARMv8-M**: Diseñado para microcontroladores, con características de seguridad mejoradas.
- **ARMv8-R**: Pensado para sistemas en tiempo real.
- **ARMv9**: Anunciado en 2021, ARMv9 introduce mejoras en el rendimiento, seguridad y capacidades de IA. Está diseñado para enfrentar los desafíos del futuro en cuanto a cómputo y proporcionar una base sólida para la próxima década de dispositivos informáticos.


## 3. Snapdragon y PowerVR
- **Snapdragon**: Familia de procesadores desarrollados por Qualcomm, ampliamente usados en dispositivos Android de gama media y alta, donde la CPU utiliza la arquitectura ARM. Ejemplos de dispositivos: 
  - Samsung Galaxy S23 (Snapdragon 8 Gen 2)
  - Xiaomi Mi 11 (Snapdragon 888)

- **PowerVR**: PowerVR es el departamento de hardware y software gráfico de Imagination Technologies. Actualmente está centrada en el diseño de chips gráficos para teléfono móvil y otros dispositivos portátiles. Ejemplo:
  - Apple iPhone 6 y iPhone SE usaban GPU PowerVR para gráficos optimizados.

## 4. Primer procesador ARM de 64 bits
El **Apple A7** fue el primer procesador ARM de 64 bits, usado por primera vez en el iPhone 5S en 2013.

## 5. Diferencias entre familias ARM Cortex

- **Cortex-A (Application)**: Para aplicaciones como teléfonos, tabletas, Smart TVs o mini PCs. Estos procesadores son capaces de correr un sistema operativo como Linux. Se pueden encontrar en placas como las Raspberry Pi, Orange Pi y BeagleBone.

- **Cortex-R (Real Time)**: Enfocado en aplicaciones de tiempo real, como equipamiento de redes de datos o en automóviles, donde se requiere respuesta rápida y procesamiento confiable.

- **Cortex-M (Microcontroller)**: Diseñado para aplicaciones sencillas, de bajo costo y consumo de energía, como dispositivos de IoT y otros sistemas embebidos.

- **Cortex-X (Custom)**: Orientado a aplicaciones más exigentes, como computadoras de escritorio. Ofrece un rendimiento máximo y personalización adicional para adaptarse a cargas de trabajo intensivas.

## 6. Diferencias entre la RAM de móviles y portátiles
- **Capacidad**: Móviles suelen tener entre 4-16 GB; portátiles hasta 64 GB o más.
- **Velocidad**: Portátiles emplean RAM DDR4/DDR5 más rápida, mientras móviles usan LPDDR5 optimizada para menor consumo.
- **Integración**: En móviles, la RAM está integrada en el chip SoC, optimizando espacio y consumo.

## 7. Tecnología microLED
La tecnología **microLED** consiste en pequeños LEDs autoemisores que ofrecen alta eficiencia energética y negros profundos, como OLED, pero con mayor durabilidad.
- **Ventajas**: Mayor brillo, eficiencia energética y durabilidad que OLED; no sufre de quemado de pantalla.
- **Desventajas**: Actualmente, su producción es costosa y compleja, limitada principalmente a prototipos o dispositivos de gama alta.

## 8. Sobre las apps que has desarrollado en esta práctica
### 9.1. ¿Qué aplicaciones has elegido?
He elegido la aplicación GPS y acelerómetro. La de cámara no la he realizado porque ya la habiamos realizado en prácticas.

### 9.2. ¿Qué clase / método destacarías de cada una de ellas?
 - En la aplicación de acelerómetro destacaria la función **onSensorChanged** y en la de GPS la función callback **configurarLocationCallback** que obtiene las coordenadas.

### 9.3. ¿Dónde encontraste la mayor dificultad?
 - En la aplicación de GPS, los callbacks es lo más difícil ya que he tenido que buscar un video en youtube que explicase (no el funcionamiento, ya que lo sé) pero que devolvia cada método y para que servian.

### 9.4. ¿Qué permisos declaraste en el archivo manifest?

```xml
    <!-- PARA LOS PERMISOS DEL ACELERÓMETRO -->
    <uses-permission android:name="android.permission.BODY_SENSORS"/>

    <!-- PARA LOS PERMISOS DEL GPS -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

