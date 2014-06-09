package model.physicalModels;

import model.Point;

/**
 *  Created by Demishev on 29.04.14.
 */
public interface Function {
    boolean isCaptured(double x, double y, double z);

    Point getNextPoint(double x, double y, double z);

    boolean canGoOn(double x, double y, double z);
}
