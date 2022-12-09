package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    //Variables globales
    Node nodo_Enemigos = new Node();
    Node cielo = new Node();
    Vector3f prueba_Ray = new Vector3f(-39,3,31);
    Vector3f prueba_Ray2 = new Vector3f(-39,3,-31);
    Vector3f prueba_Ray3 = new Vector3f(-41,3,-30);
    int bandera = 0;
    float velocidad = 10;
    static Main app;

    
    //Empezamos a definir los triggers.
    private final static Trigger TRIGGER_SHOOT = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private final static String DESTROY_ACTION = "Destroy"; 
        
    public static void main(String[] args) {
        //DEFINICION DE CONFIGURACIONES DE LA PANTALLA INICIAL
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Tower Defense");
        settings.setSettingsDialogImage("Textures/Inicial.jpeg");
        settings.setResolution(1920, 1080);
        settings.setFullscreen(true);
        settings.setFrameRate(70);
        Main app = new Main();
        
        app.setSettings(settings);
        app.start();
    }

    
    @Override
    public void simpleInitApp() {
        //LLAMAMOS AL METODO PARA CREAR LA MIRA Y GENERAR EL TERRENO
        //TAMBIEN MOVEMOS LA CAMARA Y LA VELOCIDAD
        attachCenterMark();
        generarTerreno();
        flyCam.setMoveSpeed(30f);
        cam.setLocation(new Vector3f(-20,10,50));
        
        /**
         * Añadimos el mapeo del click izquierdo
         * Y el Listener del Destruir El objeto.
         */
        inputManager.addMapping(DESTROY_ACTION, TRIGGER_SHOOT);
        inputManager.addListener(analogListener, new String[]{DESTROY_ACTION});
        
        //LLAMAMOS AL METODO GENERAR ENEMIGOS Y LO AÑADIMOS AL NODO RAIZ
        generarEnemigos();
        rootNode.attachChild(nodo_Enemigos);
        
    }
    
    
    /**
     * Método para Generar el terreno del proyecto.
     */
    private void generarTerreno(){
        nodo_Enemigos.move(39, 4, 0);
        
        Box terreno = new Box(40, 1, 30);
        Geometry geom_Terreno = new Geometry("Terreno",terreno);
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Tierra.jpg"));
        geom_Terreno.setMaterial(mat);
        rootNode.attachChild(geom_Terreno);
        
        Box puerta = new Box(1,20,30);
        Geometry geom_Puerta = new Geometry("Puerta",puerta);
        Material matPuerta = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        matPuerta.setTexture("ColorMap", assetManager.loadTexture("Textures/Puerta.jpg"));
        geom_Puerta.setMaterial(matPuerta);
        geom_Puerta.move(-39,21,0);
        rootNode.attachChild(geom_Puerta);
        
        Box entrada = new Box(1,20,30);
        Geometry geom_Entrada = new Geometry("Entrada",entrada);
        Material matEntrada = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        matEntrada.setTexture("ColorMap", assetManager.loadTexture("Textures/portal.jpg"));
        geom_Entrada.setMaterial(matEntrada);
        geom_Entrada.move(39,21,0);
        rootNode.attachChild(geom_Entrada);
        
        Box torre = new Box(4,1,4);
        Geometry geom_Torre = new Geometry("torre",torre);
        Material matTorre = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTorre.setTexture("ColorMap", assetManager.loadTexture("Textures/piedra.jpg"));
        geom_Torre.setMaterial(matTorre);
        geom_Torre.move(-20, 6, 50);
        rootNode.attachChild(geom_Torre);
        
        //CREAMOS EL CIELO
        
        Box cielo01 = new Box(200, 0, 200);
        Geometry geoCielo01 = new Geometry("Cielo01", cielo01);
        Material matCielo01 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matCielo01.setTexture("ColorMap", assetManager.loadTexture("Textures/cielo.jpeg"));
        geoCielo01.setMaterial(matCielo01);

        Box cielo02 = new Box(0, 200, 200);
        Geometry geoCielo02 = new Geometry("Cielo01", cielo02);
        Material matCielo02 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matCielo02.setTexture("ColorMap", assetManager.loadTexture("Textures/cielo.jpeg"));
        geoCielo02.setMaterial(matCielo02);

        Box cielo03 = new Box(0, 200, 200);
        Geometry geoCielo03 = new Geometry("Cielo03", cielo03);
        Material matCielo03 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matCielo03.setTexture("ColorMap", assetManager.loadTexture("Textures/cielo.jpeg"));
        geoCielo03.setMaterial(matCielo03);

        Box cielo04 = new Box(200, 200, 0);
        Geometry geoCielo04 = new Geometry("Cielo03", cielo04);
        Material matCielo04 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matCielo04.setTexture("ColorMap", assetManager.loadTexture("Textures/cielo.jpeg"));
        geoCielo04.setMaterial(matCielo04);

        Box cielo05 = new Box(200, 200, 0);
        Geometry geoCielo05 = new Geometry("Cielo03", cielo05);
        Material matCielo05 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matCielo05.setTexture("ColorMap", assetManager.loadTexture("Textures/cielo.jpeg"));
        geoCielo05.setMaterial(matCielo05);

        //MOVEMOS EL CIELO A LAS CORDENADAS
        
        geoCielo01.move(0, 200, 0);
        geoCielo02.move(200, 0, 0);
        geoCielo03.move(-200, 0, 0);
        geoCielo04.move(0, 0, 200);
        geoCielo05.move(0, 0, -200);

        //AÑADIMOS EL CIELO AL NODO RAIZ
        
        rootNode.attachChild(cielo);
        cielo.attachChild(geoCielo01);
        cielo.attachChild(geoCielo02);
        cielo.attachChild(geoCielo03);
        cielo.attachChild(geoCielo04);
        cielo.attachChild(geoCielo05);
        
    }
    
        /**
     * Método para crear la mira del juego.
     * Se crea la caja 
     * Se le da la geometria
     * El material, el color y el metodo predefinido en otro proyecto.
     * Y luego se añade al HUD.
     */
        private void attachCenterMark(){
        Box aim = new Box(Vector3f.ZERO,1,1,1);
        Geometry c = new Geometry("aim",aim);
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color",ColorRGBA.White);
        c.setMaterial(mat);
        c.scale(4);
        c.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2,0);
        guiNode.attachChild(c);
    }


    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code 
        //SE EJECUTA SIEMPRE EL METODO DE MOVER Y DE CAMBIAR EL MOVIMIENTO DEL
        //OBJETO
        cambiar(); 
        mover(tpf);
        
        
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    

    
    /**
     * Método para generar enemigos automaticamente
     */
    private void generarEnemigos(){
        Sphere enemigo = new Sphere(30, 30, 3);
        Geometry geom_Enemigo = new Geometry("Enemigo",enemigo);
        Material matEnemigo = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        matEnemigo.setTexture("ColorMap", assetManager.loadTexture("Textures/lava.jpg"));
        geom_Enemigo.setMaterial(matEnemigo);
        nodo_Enemigos.attachChild(geom_Enemigo);
        int random = (int)Math.floor(Math.random()*(20-(-20)+1)+(-20));
        geom_Enemigo.move(0, 0, random);
        rootNode.attachChild(nodo_Enemigos);
    }
    
    /**
     * Metodo para cambiar el sentido en el que se mueven los objetos
     */
    private void cambiar(){
        
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(prueba_Ray, Vector3f.UNIT_X);
        rootNode.collideWith(ray,results);
        
        //RAY PARA CAMBIAR DE UN LADO HACIA EL OTRO
        if(results.size()>0){
            Geometry target = results.getClosestCollision().getGeometry();
            if(target.getName().equals("Enemigo")){
               if(bandera == 0){
                   bandera = 1;
               }
            }
        }
        
        //RAY PARA CAMBIAR DEL OTRO LADO HACIA EL OTRO
        CollisionResults results2 = new CollisionResults();
        Ray ray2 = new Ray(prueba_Ray2, Vector3f.UNIT_X);
        rootNode.collideWith(ray2, results2);
        if(results2.size()>0){
            Geometry target2 = results2.getClosestCollision().getGeometry();
            if(target2.getName().equals("Enemigo")){
               if(bandera == 1){
                   bandera = 0;
               }
            }
        }
        
        //RAY PARA DETERMINAR SI EL ENEMIGO LLEGO A LA PUERTA
        CollisionResults results3 = new CollisionResults();
        Ray ray3 = new Ray(prueba_Ray3, Vector3f.UNIT_Z);
        rootNode.collideWith(ray3, results3);
        if(results3.size()>0){
            Geometry target3 = results3.getClosestCollision().getGeometry();
            if(target3.getName().equals("Enemigo")){
                app.stop();
            }
        }
        
    }
    
    /**
     * Analog Listener para detectar la colisión del enemigo con la camara
     */
    private final AnalogListener analogListener = new AnalogListener(){
        @Override
        public void onAnalog(String name, float intensity, float tpf) {
            if(name.equals(DESTROY_ACTION)){
                
                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(),cam.getDirection());
                rootNode.collideWith(ray,results);
                        
                if(results.size()>0){
                    Geometry target = results.getClosestCollision().getGeometry();
                    //ELIMINA AL ENEMIGO, GENERA OTRO PERO CON MAS VELOCIDAD
                    if(target.getName().equals("Enemigo")){
                        target.removeFromParent();
                        generarEnemigos();
                        velocidad = (float) (velocidad + 1.0);
                    }
                }
            }
        }  
    }; 
    
    //METODO PARA MOVER AL ENEMIGO EN EL ESPACIO.
    private void mover(float tpf){
        //BANDERA PARA SABER HACIA DONDE TIENE QUE MOVERSE
        if(bandera == 0){    
            Spatial mover = nodo_Enemigos.getChild("Enemigo");
            mover.move(-tpf*velocidad, 0, tpf*velocidad);    
            mover.rotate(0,tpf*3,0);
        }
        else{
            Spatial mover = nodo_Enemigos.getChild("Enemigo");
            mover.move(-tpf*velocidad, 0, -tpf*velocidad);  
            mover.rotate(0,tpf*3,0);
        }
    }
}
