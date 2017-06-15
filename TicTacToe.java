            import javafx.application.Application;
        import javafx.stage.Stage;
        import javafx.scene.Scene;
        import javafx.scene.Parent;
        import javafx.scene.layout.Pane;
        import javafx.scene.layout.StackPane;
        import javafx.scene.shape.Rectangle;
        import javafx.scene.shape.Line;
        import javafx.scene.paint.*;
        import javafx.scene.text.Text;
        import javafx.scene.text.Font;
        import javafx.scene.input.MouseButton;
        import javafx.geometry.Pos;
        import java.util.*;
        import javafx.animation.Timeline;
        import javafx.animation.KeyFrame;
        import javafx.animation.KeyValue;
        import javafx.util.Duration;
        import javafx.scene.control.Label;
        import javafx.scene.control.Button;
        
        public class TicTacToe extends Application
        {
            //Dimensiones de la escena
            private static int ANCHO_ESCENA = 600;
            private static int ALTO_ESCENA = 600;
        
            //Creamos una variable booleana que cambia en caso de no poder seguir jugando.
        
            private boolean jugable = true;
            //Booleano que controla el turno de cada  jugador.
            private boolean turnoX = true;
            //Creamos una booleana para acabar el turno.
            private boolean acarJuego = true;
            //Creamos el tablero de 3x3
            private Tile[][] tablero = new Tile [3][3];
            //Creamos un arrayList para ir guardando las combinaciones
            private List <Combinacion> combinaciones = new ArrayList<>();
            private Pane contenedor = new Pane();
            //Creamos el contenedor de  la escena
            private Parent contenedor()
            {
        
                //Le damos el ancho y el alto que queramos que tenga la escena.
                contenedor.setPrefSize(ANCHO_ESCENA,ALTO_ESCENA);
        
                //Creamos dos bucles anidados para que forme la cuadricula de la escena.
                for(int i = 0;i < 3;i++)
                {
                    for(int j = 0; j < 3;j++)
                    {
                        Tile cuadricula = new Tile();
                        cuadricula.setTranslateX(200 * j);//Crea las filas verticalmente
                        cuadricula.setTranslateY(200 * i);//Crea las filas verticalmente
        
                        contenedor.getChildren().add(cuadricula);
                        tablero[j][i] = cuadricula;
                    }
                }
        
                // Miramos si hay alguna combinación ganadora en las horizontales y las añadimos al arrayList.
                for (int y = 0; y < 3; y++)
                {
                    combinaciones.add(new Combinacion(tablero[0][y], tablero[1][y], tablero[2][y]));
        
                }
        
                // Miramos si hay alguna combinación ganadora en las verticales y las añadimos al arrayList.
                for (int x = 0; x < 3; x++)
                {
                    combinaciones.add(new Combinacion(tablero[x][0], tablero[x][1], tablero[x][2]));
        
                }
        
                // Miramos si hay alguna combinación ganadora en las diagonales y las añadimos al arrayList.
                combinaciones.add(new Combinacion(tablero[0][0], tablero[1][1], tablero[2][2])); //Diagonal derecha
                combinaciones.add(new Combinacion(tablero[2][0], tablero[1][1], tablero[0][2]));//Diagonal izquierda
        
                return contenedor;
            }
        
            @Override
            public void start(Stage escenario) throws Exception
            {
                //Creamos la escena y la mostramos en pantalla.
                escenario.setScene(new Scene(contenedor()));
                escenario.show();
            }
        
            /**
             * Método que comprueba el estado de las combinaciones.En caso de que la jugada finalizara la variable booleana jugable se cambia a falso
             * y se crea una línea en la combinación ganadora.
             */
            private void ComprobarCombinacion()
            {
                for(Combinacion combinacion : combinaciones)
                {
                    if(combinacion.CombinacionCompletada())
                    {
                        jugable = false;
                        animacionLinea(combinacion);
                        break;
                    }
                }
            }
        
            //Creamos las lineas que nos muestran que hemos hecho tres en raya
            private void animacionLinea(Combinacion combinacion)
            {
                Line linea = new Line();
                //Coordenada en el cual comienza la línea en el eje x y en el y
                linea.setStartX(combinacion.celdas[0].getCentroEnX());
                linea.setStartY(combinacion.celdas[0].getCentroEnY());
                //Lugar en el cual acaba la línea en ambos ejes
                linea.setEndX(combinacion.celdas[0].getCentroEnX());
                linea.setEndY(combinacion.celdas[0].getCentroEnY());
        
                //Añadimos la linea a la escena.
                contenedor.getChildren().add(linea);
        
                //Creamos un TimeLine para que la linea se vaya creando de forma animada
                Timeline timeline = new Timeline();
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                        new KeyValue(linea.endXProperty(),combinacion.celdas[2].getCentroEnX()),
                        new KeyValue(linea.endYProperty(),combinacion.celdas[2].getCentroEnY())));
                timeline.play();
                
                Label juegoTerminado = new Label("JUEGO TERMINADO");
                juegoTerminado.setTranslateX(300);
                juegoTerminado.setTranslateY(300);
                
                contenedor.getChildren().add(juegoTerminado);
               
            }
        
            private class Combinacion
            {
                //Creamos un array que contendrá las celdas del tablero
                private Tile[] celdas;
        
                public Combinacion(Tile... celdas)
                {
                    this.celdas = celdas;
                }
        
                //Método que devuelve un booleano en caso de que se hayan completado las diagonales.
                public boolean CombinacionCompletada()
                {
                    //Miramos que la celda no se encuentre vacía.
                    if(celdas[0].getFigura().isEmpty())
                    //Si la lista está vacía retornará false
                        return false;
        
                    //Devolvemos que la horizontal tiene las mismas figuras
                    return celdas[0].getFigura().equals(celdas[1].getFigura()) && celdas[0].getFigura().equals(celdas[2].getFigura());
                }
            }
        
            public class Tile extends StackPane
            {
                //Creamos las figuras mediante un objeto de tipo texto.
                private Text figura = new Text();
        
                public Tile()
                {
                    //Creamos definimos la forma que va a tener el borde de la cuadricula
                    Rectangle bordesCuadricula = new Rectangle(200,200);
                    bordesCuadricula.setFill(null);
                    bordesCuadricula.setStroke(Color.BLACK);
        
                    //Le damos propiedades a las figuras para que se vean grandes.
                    figura.setFont(Font.font(80));
        
                    setAlignment(Pos.CENTER);
        
                    //Añadimos a la escena la cuadricula y la figura que debe añadir.
                    getChildren().addAll(bordesCuadricula,figura);
        
                    //Le damos acción a los botones del ratón.
                    setOnMouseClicked(event ->{
                            if(!jugable)
                                return;
        
                            //En el caso de pulsar el  botón derecho del ratón se dibujará una X.
                            if(event.getButton() == MouseButton.PRIMARY)
                            {
                                /*Hacemos que el jugador no pueda jugar dos jugadas seguidas mediante el booelano.
                                En el caso de que el turno sea false creará la x, cambiará la booleana y comprobará si existe combinación ganadora.*/
                                if(!turnoX)
                                {
                                    acarJuego = false;
                                }
                
                                dibujarX();
                                turnoX = false;
                                ComprobarCombinacion();
                            }
                            //En el caso de pulsar el  botón izquierdo del ratón se dibujará una O.
                            else if(event.getButton() == MouseButton.SECONDARY)
                            { 
                                if(turnoX)
                                {
                                    acarJuego = false;
                                }
                                   
                                dibujarO();
                                turnoX = true;
                                ComprobarCombinacion();
                            }
                        });
                }
        
                //Método que devuelve el centro en el eje X
                public double getCentroEnX()
                {
                    return getTranslateX() + 100; //Le sumamos la mitad de lo que vale el recuadro.
                }
        
                //Método que devuelve el centro en el eje Y
                public double getCentroEnY()
                {
                    return getTranslateY() + 100; //Le sumamos la mitad de lo que vale el recuadro.
                }
        
                /**
                 * Método getter que devuelve la figura.
                 */
                public String getFigura()
                {
                    return figura.getText();
                }
        
                /**
                 * Método que dibuja la figura X
                 */
                private void dibujarX()
                {
                    figura.setText("X");
                }
        
                /**
                 * 
                 * Método que dibuja la figura O
                 */
                private void dibujarO()
                {
                    figura.setText("O");
                }
            }
        
            public static void main(String[] args)
            {
                launch(args);
            }
        }
