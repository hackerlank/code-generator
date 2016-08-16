package site.edolphin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edolphin on 16-8-16.
 */
public class Config {

    private List<String> serviceInterfacePackages = new ArrayList<>();

    public void addServiceInterfacePackage(String packagePath) {
        serviceInterfacePackages.add(packagePath);
    }

    public List<String> getServiceInterfacePackages() {
        return serviceInterfacePackages;
    }
}
