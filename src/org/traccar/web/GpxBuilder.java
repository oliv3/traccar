/*
 * Copyright 2016 Anton Tananaev (anton@traccar.org)
 * Copyright 2016 Andrey Kunitsyn (andrey@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.web;

import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;

public class GpxBuilder {

    private StringBuilder builder = new StringBuilder();
    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>"
            + "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"Traccar\" version=\"1.1\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 "
            + "http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n";
    private static final String NAME = "<name>%1$s</name><trkseg>%n";
    private static final String POINT = "<trkpt lat=\"%1$f\" lon=\"%2$f\">"
            + "<time>%3$s</time>"
            + "%4$s" /* altitude */
            + "%5$s" /* course */
            + "%6$s" /* speed */
            + "</trkpt>%n";
    private static final String ALTITUDE = "<geoidheight>%1$f</geoidheight>";
    private static final String COURSE = "<course>%1$f</course>";
    private static final String SPEED = "<speed>%1$f</speed>";
    private static final String FOOTER = "</trkseg></trk></gpx>";

    private static final DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.dateTime();

    public GpxBuilder() {
        builder.append(HEADER);
        builder.append("<trkseg>\n");
    }

    public GpxBuilder(String name) {
        builder.append(HEADER);
        builder.append(String.format(NAME, name));
    }

    public void addPosition(Position position) {
        String altitude = (position.getAltitude() != null) ? String.format(ALTITUDE, position.getAltitude()) : "";
        String course = (position.getCourse() != null) ? String.format(COURSE, position.getCourse()) : "";
        String speed = (position.getSpeed() != null) ? String.format(SPEED,
                UnitsConverter.mpsFromKnots(position.getSpeed())) : "";
        builder.append(String.format(POINT, position.getLatitude(), position.getLongitude(),
                DATE_FORMAT.print(new DateTime(position.getFixTime())), altitude, course, speed));
    }

    public void addPositions(Collection<Position> positions) {
        for (Position position : positions) {
            addPosition(position);
        }
    }

    public String build() {
        builder.append(FOOTER);
        return builder.toString();
    }

}
