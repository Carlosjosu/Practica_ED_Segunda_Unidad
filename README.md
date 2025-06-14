# Practica_ED_Segunda_Unidad

## Descripción

Este proyecto es una práctica de la Universidad Nacional de Loja (UNL) que implementa un sistema de gestión musical centrado en la administración de canciones. Utiliza Java para el backend y React con Vaadin para el frontend.

## Componentes principales utilizados

- **Cancion**: Modelo de datos que representa una canción.
- **DaoCancion**: Acceso a datos y operaciones CRUD sobre canciones, incluyendo ordenamiento y búsqueda personalizada.
- **CancionServices**: Lógica de negocio y servicios expuestos para el frontend, conectando la interfaz con la capa de datos.
- **cancion-list.tsx**: Vista principal en React/Vaadin para la gestión visual de canciones (crear, editar, eliminar, buscar y ordenar).

### Utilidades para manejo de datos (Practica3)

- **LeerData.java**: Lee y extrae datos numéricos desde archivos como `data.txt`.
- **Linkendlist.java** y **Node.java**: Implementación de lista enlazada y nodos para manipular datos de manera eficiente.
- **Quicksort.java** y **ShellSort.java**: Algoritmos de ordenamiento aplicados sobre listas enlazadas para comparar rendimiento y eficiencia.

Estas utilidades permiten cargar, procesar y ordenar grandes volúmenes de datos numéricos, facilitando pruebas y análisis de algoritmos dentro del proyecto.

## ¿Qué hace este proyecto?

- Permite registrar, editar, eliminar y listar canciones.
- Proporciona búsqueda y ordenamiento de canciones por diferentes criterios.
- Ofrece una interfaz web moderna e interactiva para la gestión musical.
- Permite cargar y ordenar datos numéricos para pruebas de algoritmos.

## Instalación y ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone <url-del-repositorio>
   cd unl-practica3
   ```
2. **Instalar dependencias frontend:**
   ```bash
   npm install
   ```
3. **Compilar y ejecutar backend:**
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
4. **Ejecutar frontend (si aplica):**
   ```bash
   npm run dev
   ```

## Uso

- Accede a la aplicación en tu navegador.
- Navega a la sección "Canciones" para gestionar el catálogo musical.
- Utiliza los formularios para crear, editar o eliminar canciones.
- Usa la barra de búsqueda y los controles de orden para filtrar y organizar la información.
- Para pruebas de algoritmos, utiliza las utilidades de Practica3 para cargar y ordenar datos desde archivos.

## Créditos

- Autor: Carlos Granda

## Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE.md para más detalles.
