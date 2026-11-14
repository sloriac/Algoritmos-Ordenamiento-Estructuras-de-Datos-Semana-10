import java.util.Scanner;

/*
 * Programa que permite al usuario llenar un arreglo estático de palabras (Strings)
 * de tamaño definido por él mismo, ordenarlo con el algoritmo de su elección
 * (selección, inserción, burbuja, mezcla o rápido) y luego buscar palabras
 * en el arreglo ordenado mediante búsqueda binaria.
 *
 * Nota: todas las comparaciones se hacen con compareToIgnoreCase(), de modo que el
 * orden es alfabético sin distinguir mayúsculas de minúsculas ("casa" y "Casa" se
 * consideran iguales). compareToIgnoreCase() devuelve:
 *   - un número negativo si la primera palabra va ANTES que la segunda,
 *   - cero si son iguales,
 *   - un número positivo si la primera palabra va DESPUÉS que la segunda.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);

    // =====================================================================================
    // PROGRAMA PRINCIPAL
    // =====================================================================================
    public static void main(String[] args) {
        //Se solicita al usuario el tamaño del arreglo y se crea el arreglo estático.
        int tamanno = leerEnteroPositivo("Ingrese la cantidad de palabras que desea guardar: ");
        String[] palabras = new String[tamanno];

        //Se llena el arreglo palabra por palabra.
        llenarArreglo(palabras);

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEnteroPositivo("Seleccione una opción: ");

            if (opcion >= 1 && opcion <= 5) {
                //Se trabaja sobre una copia del arreglo original, para que cada algoritmo
                //reciba siempre los datos desordenados tal como el usuario los ingresó.
                String[] copia = copiarArreglo(palabras);

                System.out.println("\nArreglo original: ");
                imprimirArreglo(copia);

                switch (opcion) {
                    case 1 -> ordenamientoSeleccion(copia);
                    case 2 -> ordenamientoInsercion(copia);
                    case 3 -> ordenamientoBurbuja(copia);
                    case 4 -> ordenamientoMezcla(copia);
                    case 5 -> ordenamientoRapido(copia);
                }

                System.out.println("Arreglo ordenado: ");
                imprimirArreglo(copia);

                //Una vez ordenado el arreglo, se permite realizar búsquedas binarias.
                menuBusqueda(copia);
            } else if (opcion != 6) {
                System.out.println("Opción inválida, intente de nuevo.");
            }
        } while (opcion != 6);

        System.out.println("¡Hasta luego!");
    }

    // =====================================================================================
    // SUBRUTINAS DE APOYO (entrada / salida)
    // =====================================================================================

    //Lee un número entero positivo desde el teclado, repitiendo la solicitud si el dato es inválido.
    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                int numero = Integer.parseInt(linea);
                if (numero > 0) {
                    return numero;
                }
            } catch (NumberFormatException e) {
                //Si no es un número, simplemente se vuelve a pedir.
            }
            System.out.println("Debe ingresar un número entero mayor que cero.");
        }
    }

    //Lee una palabra no vacía desde el teclado.
    private static String leerPalabra(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String palabra = sc.nextLine().trim();
            if (!palabra.isEmpty()) {
                return palabra;
            }
            System.out.println("La palabra no puede estar vacía.");
        }
    }

    //Llena cada casilla del arreglo con una palabra ingresada por el usuario.
    private static void llenarArreglo(String[] palabras) {
        for (int i = 0; i < palabras.length; i++) {
            palabras[i] = leerPalabra("Palabra " + (i + 1) + " de " + palabras.length + ": ");
        }
    }

    //Crea y devuelve una copia casilla por casilla del arreglo recibido.
    private static String[] copiarArreglo(String[] original) {
        String[] copia = new String[original.length];
        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i];
        }
        return copia;
    }

    //Imprime el contenido del arreglo en una sola línea, indicando el índice de cada casilla.
    private static void imprimirArreglo(String[] palabras) {
        for (int i = 0; i < palabras.length; i++) {
            System.out.print("[" + i + "] " + palabras[i] + "   ");
        }
        System.out.println("\n");
    }

    private static void mostrarMenu() {
        System.out.println("\n========== MENÚ DE ORDENAMIENTO ==========");
        System.out.println("1. Ordenamiento por selección   - O(n^2)");
        System.out.println("2. Ordenamiento por inserción   - O(n^2)");
        System.out.println("3. Ordenamiento por burbuja     - O(n^2)");
        System.out.println("4. Ordenamiento por mezcla      - O(n log n)");
        System.out.println("5. Ordenamiento rápido          - O(n log n) esperado, O(n^2) peor caso");
        System.out.println("6. Salir");
    }

    //Permite al usuario buscar todas las palabras que quiera en el arreglo ya ordenado.
    private static void menuBusqueda(String[] palabrasOrdenadas) {
        String respuesta;
        do {
            String buscada = leerPalabra("Ingrese la palabra que desea buscar: ");
            int posicion = busquedaBinaria(palabrasOrdenadas, buscada);

            if (posicion == -1) {
                System.out.println("La palabra \"" + buscada + "\" no se encontró en el arreglo.");
            } else {
                System.out.println("La palabra \"" + buscada + "\" se encontró en la casilla: " + posicion);
            }

            System.out.print("¿Desea buscar otra palabra? (s/n): ");
            respuesta = sc.nextLine().trim();
        } while (respuesta.equalsIgnoreCase("s"));
    }

    // =====================================================================================
    // ALGORITMOS DE ORDENAMIENTO
    // =====================================================================================

    /*
     * ORDENAMIENTO POR SELECCIÓN
     * Complejidad de tiempo: O(n^2)
     * En cada pasada i se recorre la porción restante del arreglo (n - i - 1 casillas) para
     * encontrar la palabra mínima. Hay n - 1 pasadas, por lo que el total de comparaciones es
     * (n-1) + (n-2) + ... + 1 = n(n-1)/2, que al eliminar constantes y términos no dominantes
     * resulta en O(n^2). Esto ocurre en el mejor, peor y caso esperado, ya que siempre se
     * recorre toda la porción restante, aunque el arreglo ya esté ordenado.
     */
    public static void ordenamientoSeleccion(String[] palabras) {
        //Se declaran algunas variables de control necesarias.
        int n = palabras.length;
        int menor;
        String temp;

        //El ciclo externo se usa para recorrer todo el arreglo (n - 1 pasadas).
        for (int i = 0; i < n - 1; i++) {
            //Se supone inicialmente que la palabra mínima es la de la casilla considerada.
            menor = i;

            //El ciclo interno recorre la porción restante del arreglo, omitiendo las casillas
            //ya ordenadas, para buscar la palabra mínima (la que va primero alfabéticamente).
            for (int j = i + 1; j < n; j++) {
                if (palabras[j].compareToIgnoreCase(palabras[menor]) < 0) {
                    menor = j;
                }
            }

            //Si la palabra mínima está en una casilla distinta a la considerada,
            //se intercambian los valores de ambas casillas.
            if (menor != i) {
                temp = palabras[i];
                palabras[i] = palabras[menor];
                palabras[menor] = temp;
            }
        }
    }

    /*
     * ORDENAMIENTO POR INSERCIÓN
     * Complejidad de tiempo: O(n^2)
     * Por cada una de las n palabras del arreglo original, se busca su posición en el arreglo
     * nuevo (hasta i comparaciones) y se desplazan hacia la derecha las palabras mayores
     * (hasta i movimientos). En el peor caso (arreglo en orden inverso) esto suma
     * 1 + 2 + ... + (n-1) = n(n-1)/2 operaciones, es decir O(n^2). El caso esperado también
     * es O(n^2), ya que hay dos ciclos anidados que dependen de n.
     */
    public static void ordenamientoInsercion(String[] palabras) {
        //Se crea un nuevo arreglo del mismo tamaño que el original y se inicializan
        //las variables de control necesarias.
        int n = palabras.length;
        String[] palabrasNuevo = new String[n];
        int posicionInsertar;
        int cantidadInsertados = 0;

        //El ciclo externo se usa para recorrer todo el arreglo original.
        for (int i = 0; i < n; i++) {
            //Por defecto, la palabra se inserta al final de la porción ya construida
            //(esto ocurre si es mayor que todas las palabras insertadas previamente).
            posicionInsertar = cantidadInsertados;

            //Este primer ciclo interno determina la casilla del arreglo nuevo en la que se
            //debe insertar la palabra: la primera casilla cuya palabra sea mayor o igual.
            //Sólo se recorren las casillas ya ocupadas, para no comparar contra casillas vacías (null).
            for (int j = 0; j < cantidadInsertados; j++) {
                if (palabras[i].compareToIgnoreCase(palabrasNuevo[j]) <= 0) {
                    posicionInsertar = j;
                    break;
                }
            }

            //Este segundo ciclo interno desplaza una posición hacia la derecha todas las palabras
            //del arreglo nuevo que son mayores que la palabra por insertar. Se recorre desde la
            //casilla mayor hacia la menor para no sobrescribir ningún dato prematuramente.
            for (int k = cantidadInsertados; k > posicionInsertar; k--) {
                palabrasNuevo[k] = palabrasNuevo[k - 1];
            }

            //Se inserta la palabra nueva en su posición correspondiente.
            palabrasNuevo[posicionInsertar] = palabras[i];

            //Se actualiza la variable de control tras cada iteración del ciclo externo.
            cantidadInsertados++;
        }

        //Se sustituye cada casilla del arreglo original por la correspondiente casilla
        //del arreglo nuevo, actualizando el original.
        for (int c = 0; c < n; c++) {
            palabras[c] = palabrasNuevo[c];
        }
    }

    /*
     * ORDENAMIENTO POR BURBUJA
     * Complejidad de tiempo: O(n^2)
     * Se realizan n - 1 pasadas y en cada pasada i se comparan las parejas adyacentes de la
     * porción no ordenada (n - i - 1 comparaciones). En total son n(n-1)/2 comparaciones, lo
     * que equivale a O(n^2) en el peor caso y en el caso esperado. Gracias a la bandera
     * "huboIntercambio", si el arreglo ya está ordenado el algoritmo termina tras una sola
     * pasada, por lo que su mejor caso es O(n).
     */
    public static void ordenamientoBurbuja(String[] palabras) {
        //Se declaran algunas variables de control necesarias.
        int n = palabras.length;
        String temp;
        boolean huboIntercambio;

        //El ciclo externo controla las pasadas. En cada pasada, la palabra mayor de la porción
        //restante "flota" hasta el final, por lo que queda una casilla ordenada más al final.
        for (int i = 0; i < n - 1; i++) {
            huboIntercambio = false;

            //El ciclo interno recorre la porción no ordenada comparando cada pareja de casillas
            //adyacentes. Las últimas i casillas ya están ordenadas, por eso se omiten.
            for (int j = 0; j < n - 1 - i; j++) {
                //Si hay un desorden local (la palabra de la izquierda va después que la de la
                //derecha), se corrige intercambiando ambas casillas.
                if (palabras[j].compareToIgnoreCase(palabras[j + 1]) > 0) {
                    temp = palabras[j];
                    palabras[j] = palabras[j + 1];
                    palabras[j + 1] = temp;
                    huboIntercambio = true;
                }
            }

            //Si en una pasada completa no hubo ningún intercambio, el arreglo ya está
            //ordenado y se puede detener el algoritmo anticipadamente.
            if (!huboIntercambio) {
                break;
            }
        }
    }

    /*
     * ORDENAMIENTO POR MEZCLA (MERGE SORT)
     * Complejidad de tiempo: O(n log n)
     * Es un algoritmo de "divide y vencerás":
     *   1. DIVIDIR: se parte el arreglo a la mitad repetidamente hasta tener porciones de una
     *      sola palabra (que por definición ya están ordenadas). Como cada vez se divide entre 2,
     *      hay log2(n) niveles de división.
     *   2. MEZCLAR: se combinan las porciones ordenadas de dos en dos, tomando siempre la menor
     *      de las palabras que están al frente de cada porción. Mezclar todas las porciones de
     *      un mismo nivel cuesta O(n), porque cada palabra se copia una vez por nivel.
     * Entonces se hace un trabajo O(n) en cada uno de los log n niveles: O(n log n). Esto se
     * cumple en el mejor, peor y caso esperado. Su complejidad de espacio es O(n) por el
     * arreglo auxiliar que se usa al mezclar.
     */
    public static void ordenamientoMezcla(String[] palabras) {
        //Se crea una sola vez el arreglo auxiliar que se usará en todas las mezclas
        //y se inicia la recursión sobre el arreglo completo (de la casilla 0 a la n - 1).
        String[] auxiliar = new String[palabras.length];
        mezclaRecursivo(palabras, auxiliar, 0, palabras.length - 1);
    }

    private static void mezclaRecursivo(String[] palabras, String[] auxiliar, int inicio, int fin) {
        //Caso base: si la porción tiene una sola casilla (o ninguna), ya está ordenada.
        if (inicio >= fin) {
            return;
        }

        //Se calcula la casilla central para dividir la porción en dos mitades.
        int medio = (inicio + fin) / 2;

        //Se ordena recursivamente la mitad izquierda y luego la mitad derecha.
        mezclaRecursivo(palabras, auxiliar, inicio, medio);
        mezclaRecursivo(palabras, auxiliar, medio + 1, fin);

        //Una vez ordenadas ambas mitades, se mezclan en una sola porción ordenada.
        mezclar(palabras, auxiliar, inicio, medio, fin);
    }

    private static void mezclar(String[] palabras, String[] auxiliar, int inicio, int medio, int fin) {
        //Se copia la porción que se va a mezclar al arreglo auxiliar, para poder
        //reescribir el arreglo original sin perder datos.
        for (int c = inicio; c <= fin; c++) {
            auxiliar[c] = palabras[c];
        }

        //Se declaran los índices de control:
        //i recorre la mitad izquierda, j recorre la mitad derecha y k indica la casilla
        //del arreglo original donde se colocará la siguiente palabra.
        int i = inicio;
        int j = medio + 1;
        int k = inicio;

        //Mientras queden palabras en ambas mitades, se compara la palabra al frente de cada
        //una y se coloca la menor en el arreglo original. Se usa <= para que, ante palabras
        //iguales, se tome primero la de la izquierda (esto hace al algoritmo estable).
        while (i <= medio && j <= fin) {
            if (auxiliar[i].compareToIgnoreCase(auxiliar[j]) <= 0) {
                palabras[k] = auxiliar[i];
                i++;
            } else {
                palabras[k] = auxiliar[j];
                j++;
            }
            k++;
        }

        //Si sobraron palabras en la mitad izquierda, se copian al final (ya están ordenadas).
        while (i <= medio) {
            palabras[k] = auxiliar[i];
            i++;
            k++;
        }

        //Si sobraron palabras en la mitad derecha, ya están en su lugar correcto dentro
        //del arreglo original, por lo que no hace falta copiarlas.
    }

    /*
     * ORDENAMIENTO RÁPIDO (QUICKSORT)
     * Complejidad de tiempo: O(n log n) en el caso esperado, O(n^2) en el peor caso.
     * También es un algoritmo de "divide y vencerás":
     *   1. Se elige una palabra como PIVOTE (aquí, la de la última casilla de la porción).
     *   2. PARTICIÓN: se reacomoda la porción para que todas las palabras menores o iguales al
     *      pivote queden a su izquierda y todas las mayores a su derecha. Así el pivote queda
     *      en su posición final definitiva. Particionar cuesta O(n).
     *   3. Se aplica recursivamente el mismo proceso a la parte izquierda y a la derecha.
     * Si el pivote divide la porción en dos partes de tamaño similar, hay log n niveles con
     * trabajo O(n) cada uno: O(n log n). En el peor caso (por ejemplo, cuando el arreglo ya
     * está ordenado y el pivote siempre es el mayor), cada partición sólo separa una casilla,
     * habiendo n niveles: n + (n-1) + ... + 1 = O(n^2).
     */
    public static void ordenamientoRapido(String[] palabras) {
        //Se inicia la recursión sobre el arreglo completo.
        rapidoRecursivo(palabras, 0, palabras.length - 1);
    }

    private static void rapidoRecursivo(String[] palabras, int inicio, int fin) {
        //Caso base: si la porción tiene una sola casilla (o ninguna), ya está ordenada.
        if (inicio >= fin) {
            return;
        }

        //Se particiona la porción y se obtiene la posición final en la que quedó el pivote.
        int posicionPivote = particion(palabras, inicio, fin);

        //Se ordenan recursivamente las palabras a la izquierda y a la derecha del pivote.
        //El pivote ya no se incluye porque ya está en su lugar definitivo.
        rapidoRecursivo(palabras, inicio, posicionPivote - 1);
        rapidoRecursivo(palabras, posicionPivote + 1, fin);
    }

    private static int particion(String[] palabras, int inicio, int fin) {
        //Se elige como pivote la palabra de la última casilla de la porción.
        String pivote = palabras[fin];

        //i marca el final de la zona de palabras menores o iguales al pivote.
        //Inicia una casilla antes del inicio porque esa zona empieza vacía.
        int i = inicio - 1;
        String temp;

        //Se recorre la porción (sin incluir el pivote). Cada palabra menor o igual al pivote
        //se intercambia hacia la zona izquierda, haciendo crecer dicha zona en una casilla.
        for (int j = inicio; j < fin; j++) {
            if (palabras[j].compareToIgnoreCase(pivote) <= 0) {
                i++;
                temp = palabras[i];
                palabras[i] = palabras[j];
                palabras[j] = temp;
            }
        }

        //Finalmente se coloca el pivote justo después de la zona de palabras menores,
        //que es su posición definitiva en el arreglo ordenado.
        temp = palabras[i + 1];
        palabras[i + 1] = palabras[fin];
        palabras[fin] = temp;

        //Se retorna la posición final del pivote.
        return i + 1;
    }

    // =====================================================================================
    // ALGORITMO DE BÚSQUEDA
    // =====================================================================================

    /*
     * BÚSQUEDA BINARIA
     * Complejidad de tiempo: O(log n)
     * Requiere que el arreglo esté ordenado. En cada iteración se compara la palabra buscada
     * con la del pivote (casilla central) y se descarta la mitad del intervalo en la que es
     * imposible que esté. Así, el intervalo pasa de n a n/2, n/4, n/8, ... hasta llegar a 1
     * casilla, lo que toma como máximo log2(n) + 1 iteraciones. Por ejemplo, en un arreglo de
     * 1000 palabras se necesitan a lo sumo unas 10 comparaciones, mientras que una búsqueda
     * lineal podría necesitar 1000. El mejor caso es O(1), cuando la palabra está en el
     * primer pivote.
     * Retorna el índice de la casilla donde se encontró la palabra, o -1 si no se encontró.
     */
    public static int busquedaBinaria(String[] palabras, String palabraBuscada) {
        //Se declaran e inicializan las variables de control necesarias:
        //el intervalo de búsqueda inicia abarcando todo el arreglo.
        int limiteMenor = 0;
        int limiteMayor = palabras.length - 1;
        int pivote;
        int comparacion;

        //El ciclo se usa para seguir reduciendo el intervalo de búsqueda mientras los límites
        //no se traslapen (es decir, mientras quede al menos una casilla por revisar).
        while (limiteMenor <= limiteMayor) {
            //Se define el pivote en la casilla central del intervalo actual.
            pivote = (limiteMenor + limiteMayor) / 2;

            //Se compara la palabra del pivote con la palabra buscada.
            comparacion = palabras[pivote].compareToIgnoreCase(palabraBuscada);

            //Si son iguales, se encontró la palabra y se retorna la casilla del pivote.
            if (comparacion == 0) {
                return pivote;
            }

            //Se ajusta alguno de los dos límites del intervalo en función de la comparación:
            if (comparacion > 0) {
                //La palabra del pivote va después de la buscada: se descarta la mitad derecha.
                limiteMayor = pivote - 1;
            } else {
                //La palabra del pivote va antes de la buscada: se descarta la mitad izquierda.
                limiteMenor = pivote + 1;
            }
        }

        //Si los límites se traslaparon, la palabra no está en el arreglo.
        return -1;
    }
}
