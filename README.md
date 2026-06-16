# Investigation UR-OS

![Estado](https://img.shields.io/badge/estado-en_progreso-yellow)
![Lenguaje](https://img.shields.io/badge/lenguaje-Java-blue)

## Resumen

`Investigation-UR-OS` es un proyecto de investigación y desarrollo de un sistema operativo educativo para la Universidad Regional. El repositorio organiza la implementación de estructuras de memoria, planificación de procesos, gestión de recursos y simulaciones en Java.

## Objetivos del proyecto

- Desarrollar componentes de un sistema operativo educativo.
- Investigar y comparar técnicas de memoria y planificación.
- Mantener una estructura de código ordenada y documentada.
- Facilitar la colaboración mediante buenas prácticas y estándares.

## Contenido principal

- `src/` - Código fuente en Java.
- `build.xml` - Script de compilación Ant.
- `docs/` - Documentación del proyecto y hoja de ruta.
- `CONTRIBUTING.md` - Guía para contribuir.
- `simulations/` - Archivos de configuración para simulaciones.
- `data/` - Datos de entrada y resultados de ejemplo.
- `build/` - Productos de compilación generados.

## Estructura del código

- `src/ur_os/` - Clases principales del sistema operativo.
- `src/ur_os/memory/` - Módulos de gestión de memoria.
- `src/ur_os/process/` - Modelo y planificación de procesos.
- `src/ur_os/resource/` - Administración de recursos y archivos.
- `src/ur_os/system/` - Componentes del sistema y CPU.
- `src/ur_os/txtFileManager/` - Lectura y comparación de archivos.

## Requisitos

- Java JDK 8 o superior.
- Apache Ant.
- Git.

## Instalación y uso

1. Clona el repositorio:
   ```bash
   git clone https://github.com/<usuario>/Investigation-UR-OS.git
   cd Investigation-UR-OS
   ```

2. Compila el proyecto con Ant:
   ```bash
   ant
   ```

3. Ejecuta la aplicación desde NetBeans o con el comando de ejecución configurado en tu IDE.

> Nota: Este proyecto está concebido para ejecutarse principalmente desde un entorno Java/Ant como NetBeans.

## Ejecución de pruebas

Actualmente no se incluyen pruebas automatizadas formales en el repositorio. Para validar el proyecto, revisa los archivos de simulación en `simulations/` y los escenarios de `src/ur_os/`.

## Buenas prácticas

- Usa ramas temáticas para cada cambio, por ejemplo `feature/<descripcion>` o `fix/<descripcion>`.
- Mantén los mensajes de commit claros y descriptivos.
- Revisa `CONTRIBUTING.md` antes de enviar pull requests.
- Documenta cualquier dependencia adicional y los pasos de configuración.

## Hoja de ruta y documentación

Consulta los documentos en `docs/` para la visión general, objetivos y fases de desarrollo:

- `docs/PROJECT_OVERVIEW.md` - Visión general del proyecto.
- `docs/ROADMAP.md` - Plan de trabajo y fases.

## Cómo contribuir

1. Revisa `CONTRIBUTING.md`.
2. Crea una rama a partir de `main`.
3. Realiza tus cambios y añade documentación si es necesario.
4. Envía un pull request describiendo el alcance y los resultados.

## Contacto

Para preguntas, ideas o colaboración, abre un issue en el repositorio o contacta con el equipo del proyecto.
