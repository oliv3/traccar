/*
 * Copyright 2012 - 2015 Anton Tananaev (anton@traccar.org)
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
package org.traccar.geocoder;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class BanGeocoder extends JsonGeocoder {

    // private static final String FORMAT_URL = "https://api-adresse.data.gouv.fr/reverse/?lat=%f&lon=%f";
    private static final String FORMAT_URL = "http://api-adresse.data.gouv.fr/reverse/?lat=%f&lon=%f";

    public BanGeocoder(int cacheSize) {
        super(FORMAT_URL, cacheSize);
    }

    @Override
    public Address parseAddress(JsonObject json) {
        JsonArray result = json.getJsonArray("features");

        if (result != null) {
            JsonArray locations = result.getJsonObject(0).getJsonArray("properties");

            if (locations != null) {
                JsonObject location = locations.getJsonObject(0);
                Address address = new Address();
                address.setCountry("FR");

                if (location.containsKey("street")) {
                    address.setStreet(location.getString("street"));
                }
                if (location.containsKey("citycode")) {
                    address.setPostcode(location.getString("citycode"));
                }
                if (location.containsKey("city")) {
                    address.setSettlement(location.getString("city"));
                }
                if (location.containsKey("housenumber")) {
                    address.setHouse(location.getString("housenumber"));
                }
                if (location.containsKey("postcode")) {
                    address.setPostcode(location.getString("postcode"));
                }

                return address;
                }
            }

            return null;
      }

}
