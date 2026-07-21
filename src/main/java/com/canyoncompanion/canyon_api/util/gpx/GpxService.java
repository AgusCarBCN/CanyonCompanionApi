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


                gpxContent.append("  <wpt lat=\"").append(waypoint.getLatitude()).append("\" lon=\"").append(waypoint.getLongitude()).append("\">\n");


                if (waypoint.getElevation() != null) {
                    gpxContent.append("    <ele>").append(waypoint.getElevation()).append("</ele>\n");
                }


                gpxContent.append("    <desc>").append(waypoint.getDescription()).append("</desc>\n");


                gpxContent.append("    <sym>").append(waypoint.getSymbol()).append("</sym>\n");


                if (waypoint.getImagePath() != null) {

                    gpxContent.append("    <link href=\"").append(waypoint.getImagePath()).append("\">\n");

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

                gpxContent.append("      <trkpt lat=\"").append(point.getLatitude()).append("\" lon=\"").append(point.getLongitude()).append("\">\n");


                if (point.getElevation() != null) {

                    gpxContent.append("        <ele>").append(point.getElevation()).append("</ele>\n");
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