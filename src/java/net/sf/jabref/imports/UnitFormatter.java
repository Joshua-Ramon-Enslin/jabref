/*  Copyright (C) 2012 JabRef contributors.
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package net.sf.jabref.imports;

import java.util.Arrays;
import java.util.Comparator;

import net.sf.jabref.export.layout.LayoutFormatter;

public class UnitFormatter implements LayoutFormatter {
    
    private String[] unitList = new String[] {
        "A",  // Ampere
        "Ah",  // Ampere hours
        "B", // Byte
        "Bq", // Bequerel
        "C", // Coulomb
        "F",  // Farad
        "Gy",  // Gray
        "H",  // Henry
        "Hz", // Hertz
        "J", // Joule
        "K", // Kelvin
        "N", // Newton
        "\\$\\\\Omega\\$", // Ohm
        "Pa", // Pascal
        "S", // Siemens, Samples
        "Sa", // Samples
        "Sv",  // Sv
        "T", // Tesla
        "V", // Volt
        "VA", // Volt ampere
        "W",  // Watt
        "Wb", // Weber
        "Wh",  // Watt hours
        "b",  // bit
        "cd", // candela
        "dB",  // decibel
        "dBm", // decibel
        "dBc",  //decibel
        "eV",  // electron volts
        "kat", // katal
        "lm",  // lumen
        "lx",  // lux
        "m",  // meters
        "mol", // mol
        "rad",  // radians
        "s",  // seconds
        "sr", // steradians
    };
    
    private String[] unitPrefixList = new String[] {
        "y", // yocto
        "z", // zepto
        "a", // atto
        "f", // femto
        "p", // pico
        "n", // nano
        "\\$\\\\mu\\$", // micro
        "u",  // micro
        "m",  // milli
        "c",  // centi
        "d",  // deci
        "",  // no prefix
        "da",  // deca
        "h",  // hekto
        "k",  // kilo
        "M",  // mega
        "G",  // giga
        "T",  // tera
        "P",  // peta
        "E",  // exa
        "Z", // zetta
        "Y", // yotta
    };
    
    private String[] unitCombinations;
    
    public UnitFormatter() {
	super();
        int uLLength = unitList.length;
        int uPLLength = unitPrefixList.length;
        int uCLength = uLLength*uPLLength;
        unitCombinations = new String[uCLength];
        for(int i = 0; i < uLLength; i++) {
            for(int j = 0; j < uPLLength; j++) {
                unitCombinations[i*uPLLength+j] = unitPrefixList[j] + unitList[i];
            }
        }
        
    }
    
    public String format(String text, String [] listOfWords) {
	if (text == null) {
	    return null;
        }
        
        Arrays.sort(listOfWords, new LengthComparator());  // LengthComparator from CaseKeeper.java
        
        // Replace the hyphen in 12-bit etc with a non-breaking hyphen, will also avoid bad casing of 12-Bit
        text = text.replaceAll("([0-9,\\.]+)-([Bb][Ii][Tt])","$1\\\\mbox\\{-\\}$2");
        
        // Replace the space in 12 bit etc with a non-breaking space, will also avoid bad casing of 12 Bit
        text = text.replaceAll("([0-9,\\.]+) ([Bb][Ii][Tt])","$1~$2");
        
        // For each word in the list
	for (int i = 0; i < listOfWords.length; i++) {
            // Add {} if the character before is a space, -, /, (, [, or } or if it is at the start of the string but not if it is followed by a }
	    text = text.replaceAll("([0-9,\\.]+)("+listOfWords[i]+")","$1\\{$2\\}"); // Only add brackets to keep case
            text = text.replaceAll("([0-9,\\.]+)-("+listOfWords[i]+")","$1\\\\mbox\\{-\\}\\{$2\\}"); // Replace hyphen with non-break hyphen
            text = text.replaceAll("([0-9,\\.]+) ("+listOfWords[i]+")","$1~\\{$2\\}"); // Replace space with a hard space
            
	}
        
        return text;
    }
    

    public String format(String text) {
	if (text == null) {
	    return null;
        }
        return this.format(text,unitCombinations);
    }
    

}
