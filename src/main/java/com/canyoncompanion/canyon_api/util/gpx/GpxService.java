package com.canyoncompanion.canyon_api.util.gpx;

import com.canyoncompanion.canyon_api.dtos.requests.route.TrackPointRequestDTO;
import com.canyoncompanion.canyon_api.model.entities.WaypointEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class GpxService {


    public String createGpxFile(
            String fileName,
            List<TrackPointRequestDTO> trackPoints,
            List<WaypointEntity> waypoints
    ) {


        Path gpxDirectory =
                Paths.get("/data/routes/gpx");


        try {

            if (!Files.exists(gpxDirectory)) {
                Files.createDirectories(gpxDirectory);
            }


            Path filePath =
                    gpxDirectory.resolve(fileName + ".gpx");


            StringBuilder gpxContent =
                    new StringBuilder();


            gpxContent.append(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            );

            gpxContent.append(
                    "<gpx version=\"1.1\" " +
                            "creator=\"CanyoningCompanion\" " +
                            "xmlns=\"http://www.topografix.com/GPX/1/1\">\n"
            );


            // WAYPOINTS

            for (WaypointEntity waypoint : waypoints) {


                gpxContent.append(
                        "  <wpt lat=\""
                                + waypoint.getLatitude()
                                + "\" lon=\""
                                + waypoint.getLongitude()
                                + "\">\n"
                );


                if (waypoint.getElevation() != null) {
                    gpxContent.append(
                            "    <ele>"
                                    + waypoint.getElevation()
                                    + "</ele>\n"
                    );
                }


                gpxContent.append(
                        "    <desc>"
                                + waypoint.getDescription()
                                + "</desc>\n"
                );


                gpxContent.append(
                        "    <sym>"
                                + waypoint.getSymbol()
                                + "</sym>\n"
                );


                if (waypoint.getImagePath() != null) {

                    gpxContent.append(
                            "    <link href=\""
                                    + waypoint.getImagePath()
                                    + "\">\n"
                    );

                    gpxContent.append(
                            "      <type>image/jpeg</type>\n"
                    );

                    gpxContent.append(
                            "    </link>\n"
                    );
                }


                gpxContent.append(
                        "  </wpt>\n"
                );
            }


            // TRACK

            gpxContent.append("  <trk>\n");
            gpxContent.append("    <name>Route</name>\n");
            gpxContent.append("    <trkseg>\n");


            for (TrackPointRequestDTO point : trackPoints) {

                gpxContent.append(
                        "      <trkpt lat=\""
                                + point.getLatitude()
                                + "\" lon=\""
                                + point.getLongitude()
                                + "\">\n"
                );


                if (point.getElevation() != null) {

                    gpxContent.append(
                            "        <ele>"
                                    + point.getElevation()
                                    + "</ele>\n"
                    );
                }


                gpxContent.append(
                        "      </trkpt>\n"
                );
            }


            gpxContent.append("    </trkseg>\n");
            gpxContent.append("  </trk>\n");
            gpxContent.append("</gpx>\n");


            Files.writeString(
                    filePath,
                    gpxContent.toString(),
                    StandardCharsets.UTF_8
            );


            return filePath.toString();


        } catch (IOException e) {

            throw new RuntimeException(
                    "Error creating GPX file",
                    e
            );
        }
    }
}