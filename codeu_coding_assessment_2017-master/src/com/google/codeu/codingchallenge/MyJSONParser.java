// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See cd  License for the specific language governing permissions and
// limitations under the License.

import java.io.IOException;
import java.util.Stack;

final class MyJSONParser implements JSONParser {

    @Override
    public JSON parse(String in) throws IOException {
        int level = 0;
        JSON json = new MyJSON(); // is this right
        boolean inString = false;
        boolean inJSON = false;
        boolean isKey = true;
        String key = "";
        Object value = null;
        StringBuilder strbuilder = new StringBuilder();
        if (in.replaceAll(" ","").compareTo("{}") == 0) {
            return new MyJSON();
        }
        if (in.charAt(0) != '{') { // won't be a valid string if there is something other here
            throw new IOException("Invalid String without { to begin");
        }
        for (int i = 0; i < in.length() - 1; i++) {
            if (inString || inJSON) {
                strbuilder.append(in.charAt(i));
                if (in.charAt(i) == '{' && inJSON) {
                    level++;
                }
                if (in.charAt(i) == '}') {
                    level--;
                    strbuilder.append(i);
                    if (level < 0) {
                        throw new IOException("Invalid String with more closing brackets");
                    }
                    if (level == 1) {
                        inJSON = false;
                        value = parse(strbuilder.toString());
                    }
                }
                if (in.charAt(i) == '\"' && inString) {
                    if (!(in.charAt(i-1) == '\\')) {
                        inString = false;
                    }
                    if (in.charAt(i) == ':') {
                        if (inJSON) { // there's not supposed to be colons in the JSON
                            throw new IOException("There should not be colons in the JSON");
                        }
                        isKey = false; // if it follows a colon then it's not a key anymore
                        inJSON = true;
                    }
                } else {
                    if (in.charAt(i) == ',') {
                        if (isKey) {
                            throw new IOException();
                        }
                        if (value instanceof String) {
                            json.setString(key,(String)value);
                        } else {
                            json.setObject(key,(JSON)value);
                        }
                        inJSON = false;
                        isKey = true;
                    }
                    else if (in.charAt(i) == '\"') {
                        inString = true;
                    }
                    else if (level < 0) {
                        throw new IOException();
                    }
                    else if (in.charAt(i) == '{') {
                        if (isKey) {
                            if (level > 1) {
                                throw new IOException("Key cannot be a JSON object");
                            }
                        }
                        else if (in.charAt(i) == ':') { // the end of key
                            System.out.println(key);
                            key = strbuilder.toString();
                        }
                        inJSON = true;
                        level++;
                        strbuilder.append(i);
                        if (level == 1) {
                            inJSON = false;
                        }
                    }
                    else if (level == 1) {
                        strbuilder.setLength(0);
                    }
                    else if (in.charAt(i) != ' ') {
                        throw new IOException("Handle character outside everything:" + in.charAt(i));
                    }
                }
            }
        }
        return json;
    }
}