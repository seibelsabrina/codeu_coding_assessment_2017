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
       int openBraces = 0;
    char currentChar;
    String currentKey = "";

    JSON currentObject = new MyJSON();
    Stack nestedObjects = new Stack();
    boolean inKey = false;
    boolean inStringValue = false;

    for (int i = 1; i < in.length(); i++) {
        currentChar = in.charAt(i);
        if (currentChar == ' ') {
            continue;
        }
        if (!(inKey) && currentChar == '\"') {
            inStringValue = true;
            inKey = true;
        }
        if (currentChar == '{') {
            currentObject = new MyJSON();
            nestedObjects.push(currentObject);
            openBraces++;
        }
        if (currentChar == '}') {
            if (openBraces <0) {
                throw new IOException("Invalid String: Too many closing braces.");
            }
            openBraces--;
            nestedObjects.pop();
            nestedObjects.peek().setObject(currentKey,currentObject);
        }
        if (inKey) {
            currentKey += currentChar;
        }
        if (currentChar == "\"") {
            continue;
        }
    }
    return currentObject;
}
}