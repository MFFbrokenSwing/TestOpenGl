package brokenswing.utils;

import java.util.ArrayList;

public class ResourcesManager {

    private static volatile ResourcesManager instance = null;

    public static ResourcesManager instance() {
        if(instance == null) {
            synchronized (ResourcesManager.class) {
                if(instance == null)
                    instance =  new ResourcesManager();
            }
        }
        return instance;
    }

    private final ArrayList<IResource> resources;

    private ResourcesManager() {
        resources = new ArrayList<>();
    }

    public void addResource(IResource resource) {
        resources.add(resource);
    }

    public void clearResources() {
        for (IResource resource : resources) {
            resource.unbind();
            resource.delete();
        }
    }

}
