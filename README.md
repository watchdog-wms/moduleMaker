#### MANUAL

The most recent version of the manual is can be found in the **distribute/** folder.

#### INSTALLATION  
##### Manually:

  - Download a release from https://github.com/watchdog-wms/moduleMaker/releases
  - Extract and rename the **distribute/** folder to **moduleMaker/** and move it into the 
  Watchdog installation folder (e.g. /path/to/watchdog/moduleMaker/).
  
##### Automatically:
Alternatively, you can run **helper_scripts/downloadModuleMaker.sh** located in
  the Watchdog installation directory to download and install it automatically.

#### RUN MODULE MAKER

The distributed jar files are build for Java 11 and are launched internally 
by a Bash script. In order to start the moduleMaker, change into the 
**moduleMaker/** directory and call

./moduleMaker.sh

The moduleMaker depends on the JavaFX SDK. The Bash script will try 
to identify the install location of the JavaFX SDK automatically.

#### CONTACT
If you have any questions or suggestions, please feel free to contact me:
michael.kluge@bio.ifi.lmu.de

In case of bugs or feature requests, feel free to create an issues at
https://github.com/watchdog-wms/moduleMaker/issues

#### LICENCE
ModuleMaker is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ModuleMaker is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ModuleMaker.  If not, see <http://www.gnu.org/licenses/>.
