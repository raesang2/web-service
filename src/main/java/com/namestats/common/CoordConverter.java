package com.namestats.common;

import org.locationtech.proj4j.*;

public class CoordConverter {

    private static final CoordinateTransform transform;

    static {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem epsg5174 = factory.createFromName("EPSG:5174");
        CoordinateReferenceSystem wgs84 = factory.createFromName("EPSG:4326");
        transform = new CoordinateTransformFactory().createTransform(epsg5174, wgs84);
    }

    public static double[] convert(double x, double y) {
        ProjCoordinate src = new ProjCoordinate(x, y);
        ProjCoordinate dst = new ProjCoordinate();
        transform.transform(src, dst);
        return new double[]{dst.y, dst.x}; // [latitude, longitude]
    }
}
