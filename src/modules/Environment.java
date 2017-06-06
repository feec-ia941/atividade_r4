package modules;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ws3dproxy.CommandUtility;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.Thing;
import ws3dproxy.model.World;
import ws3dproxy.util.Constants;

public class Environment extends EnvironmentImpl {

    private static final int DEFAULT_TICKS_PER_RUN = 100;
    private int ticksPerRun;
    private WS3DProxy proxy;
    private Creature creature;
    private Thing food;
    private Thing jewel;
    private Thing brink;
    private List<Thing> thingAhead;
    private Thing leafletJewel;
    private String currentAction;

    public Environment() {
        this.ticksPerRun = DEFAULT_TICKS_PER_RUN;
        this.proxy = new WS3DProxy();
        this.creature = null;
        this.food = null;
        this.jewel = null;
        this.brink = null;
        this.thingAhead = new ArrayList<>();
        this.leafletJewel = null;
        this.currentAction = "rotate";
    }

    @Override
    public void init() {
        super.init();
        ticksPerRun = (Integer) getParam("environment.ticksPerRun", DEFAULT_TICKS_PER_RUN);
        taskSpawner.addTask(new BackgroundTask(ticksPerRun));

        try {
            System.out.println("Reseting the WS3D World ...");
            World world = proxy.getWorld();
            world.reset();
            creature = proxy.createCreature(150, 300, 1);
            creature.start();

            int height = world.getEnvironmentHeight();
            int width = world.getEnvironmentWidth();

            CommandUtility.sendNewBrick(1, 419, 320, 425.0, 595);
            CommandUtility.sendNewBrick(1, 259, 6, 264, 311);
            CommandUtility.sendNewBrick(1, 577, 24, 582, 302);

            System.out.println("Starting the WS3D Resource Generator ... ");

            //World.grow(1);
            Thread.sleep(4000);
            creature.updateState();
            System.out.println("DemoLIDA has started...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class BackgroundTask extends FrameworkTaskImpl {

        public BackgroundTask(int ticksPerRun) {
            super(ticksPerRun);
        }

        @Override
        protected void runThisFrameworkTask() {
            updateEnvironment();
            performAction(currentAction);
        }
    }

    @Override
    public void resetState() {
        currentAction = "rotate";
    }

    @Override
    public Object getState(Map<String, ?> params) {
        Object requestedObject = null;
        String mode = (String) params.get("mode");
        switch (mode) {
            case "food":
                requestedObject = food;
                break;
            case "jewel":
                requestedObject = jewel;
                break;
            case "brink":
                requestedObject = brink;
                break;
            case "thingAhead":
                requestedObject = thingAhead;
                break;
            case "leafletJewel":
                requestedObject = leafletJewel;
                break;
            default:
                break;
        }
        return requestedObject;
    }

    public void updateEnvironment() {
        creature.updateState();
        food = null;
        jewel = null;
        leafletJewel = null;
        brink = null;
        thingAhead.clear();
        List<Thing> listThings = creature.getThingsInVision();

        for (Thing thing : listThings) {
            if (creature.calculateDistanceTo(thing) <= Constants.OFFSET) {
                // Identifica o objeto proximo
                thingAhead.add(thing);
                break;
            } else if (thing.getCategory() == Constants.categoryJEWEL) {
                if (leafletJewel == null) {
                    // Identifica se a joia esta no leaflet
                    for (Leaflet leaflet : creature.getLeaflets()) {
                        if (leaflet.ifInLeaflet(thing.getMaterial().getColorName())
                                && leaflet.getTotalNumberOfType(thing.getMaterial().getColorName()) > leaflet.getCollectedNumberOfType(thing.getMaterial().getColorName())) {
                            leafletJewel = thing;
                            break;
                        }
                    }
                } else {
                    // Identifica a joia que nao esta no leaflet
                    jewel = thing;
                }
            } else if (food == null
                    && (thing.getCategory() == Constants.categoryFOOD
                    || thing.getCategory() == Constants.categoryPFOOD
                    || thing.getCategory() == Constants.categoryNPFOOD)) {

                // Identifica qualquer tipo de comida
                food = thing;
            } else if (thing.getCategory() == Constants.categoryBRICK) {
                brink = thing;
            }

        }
    }

    @Override
    public void processAction(Object action) {
        String actionName = (String) action;
        currentAction = actionName.substring(actionName.indexOf(".") + 1);
    }

    String nameBrinck = "";

    double x1 = 0;
    double x2 = 0;
    double y1 = 0;
    double y2 = 0;

    List<String> listBrink = new ArrayList<>();

    private void performAction(String currentAction) {
        try {
            //System.out.println("Action: "+currentAction);
            switch (currentAction) {
                case "rotate":

                    if (food == null) {
                        creature.rotate(3);
                    }

                    break;

                case "gotoFood":
                    if (food != null) {
                        double x = food.getX1();
                        double y = food.getY1();
                        creature.moveto(1.5,x , y);
                    }
                    //CommandUtility.sendGoTo(creature.getIndex(), 3.0, 3.0, food.getX1(), food.getY1());
                    break;
                case "gotoJewel":
                    if (leafletJewel != null) {
                        creature.moveto(0.1, leafletJewel.getX1(), leafletJewel.getY1());
                    }
                    //CommandUtility.sendGoTo(creature.getIndex(), 3.0, 3.0, leafletJewel.getX1(), leafletJewel.getY1());
                    break;
                case "get":
                    creature.move(0.0, 0.0, 0.0);
                    //CommandUtility.sendSetTurn(creature.getIndex(), 0.0, 0.0, 0.0);
                    if (thingAhead != null) {
                        for (Thing thing : thingAhead) {
                            if (thing.getCategory() == Constants.categoryJEWEL) {
                                creature.putInSack(thing.getName());
                            } else if (thing.getCategory() == Constants.categoryFOOD || thing.getCategory() == Constants.categoryNPFOOD || thing.getCategory() == Constants.categoryPFOOD) {
                                creature.eatIt(thing.getName());

                            }
                        }
                    }
                    this.resetState();
                    break;
                case "goBrinck":

                    if (brink.getName() != nameBrinck) {

                        nameBrinck = brink.getName();

                        x1 = brink.getX1();
                        x2 = brink.getX2();
                        y1 = brink.getY1();
                        y2 = brink.getY2();

                        double distanciCreatureY1 = GetGeometricDistanceToCreature(x1, y1, x1, y1, creature.getPosition().getX(), creature.getPosition().getY());
                        double distanciCreatureY2 = GetGeometricDistanceToCreature(x2, y2, x2, y2, creature.getPosition().getX(), creature.getPosition().getY());

                        if (distanciCreatureY1 < distanciCreatureY2) {
                            //creature.moveto(0.7, x1 , y1 - 180);

                            if (!listBrink.contains(brink.getName())) {
                                World.createFood(0, x1, y1 - 200);
                                listBrink.add(brink.getName());
                            }
                        } else {
                            //creature.moveto(0.7, x2 , y2 + 100);

                            if (!listBrink.contains(brink.getName())) {
                                World.createFood(1, x2, y2 + 200);
                                listBrink.add(brink.getName());
                            }
                        }

                        // if(distanciCreature < 200)
                        System.err.println("" + brink.getName());
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double GetGeometricDistanceToCreature(double x1, double y1, double x2, double y2, double xCreature, double yCreature) {
        float squared_dist = 0.0f;
        double maxX = Math.max(x1, x2);
        double minX = Math.min(x1, x2);
        double maxY = Math.max(y1, y2);
        double minY = Math.min(y1, y2);

        if (xCreature > maxX) {
            squared_dist += (xCreature - maxX) * (xCreature - maxX);
        } else if (xCreature < minX) {
            squared_dist += (minX - xCreature) * (minX - xCreature);
        }

        if (yCreature > maxY) {
            squared_dist += (yCreature - maxY) * (yCreature - maxY);
        } else if (yCreature < minY) {
            squared_dist += (minY - yCreature) * (minY - yCreature);
        }

        return Math.sqrt(squared_dist);
    }
}
