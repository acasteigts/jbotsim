/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.jbotsim.io.format.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.NoSuchElementException;

/**
 * This enumeration lists all names of elements and attributes used in JBotSim XML documents. By convention we suffixed
 * attributes with <code>_ATTR</code>. The class also offers some helper methods used by parsers and builders.
 */
public enum XMLKeys {
    JBOTSIM("jbotsim"),
    TOPOLOGY("topology"),
    CLASSES("classes"),
    NODECLASS("node-class"),
    NODE("node"),
    LINK("link"),
    GRAPH("graph"),
    LINK_RESOLVER("link-resolver"),
    MESSAGE_ENGINE("message-engine"),
    SCHEDULER("scheduler"),
    CLOCKCLASS("clock-class"),

    VERSION_ATTR("version"),
    IDENTIFIER_ATTR("id"),
    DIRECTED_ATTR("directed"),
    CLASS_ATTR("class"),
    SENSING_RANGE_ATTR("sensing-range"),
    COMMUNICATION_RANGE_ATTR("communication-range"),
    WIRELESS_ENABLED_ATTR("wireless-enabled"),
    SOURCE_ATTR("src"),
    DESTINATION_ATTR("dst"),
    DIRECTION_ATTR("direction"),
    TIME_UNIT_ATTR("speed"),

    NODECLASS_ATTR("node-class"),

    WIDTH_ATTR("width"),
    HEIGHT_ATTR("height"),
    COLOR_ATTR("color"),
    ICON_ATTR("icon"),
    SIZE_ATTR("size"),
    LOCATION_X_ATTR("x"),
    LOCATION_Y_ATTR("y"),
    LOCATION_Z_ATTR("z"),

    TRACE("trace"),
    ADD_NODE("add-node"),
    DELETE_NODE("delete-node"),
    SELECT_NODE("select-node"),
    MOVE_NODE("move-node"),
    START_TOPOLOGY("start-topology"),
    TIME_ATTR("time")
    ;

    public interface Function<IType, RType> {
        RType apply(IType in);
    }

    public static final Function<String, Boolean> BooleanFromString = new Function<String, Boolean>() {
        @Override
        public Boolean apply(String i) {
            return Boolean.valueOf(i);
        }
    };

    public static final Function<String, Integer> IntegerFromString = new Function<String, Integer>() {
        @Override
        public Integer apply(String i) {
            return Integer.valueOf(i);
        }
    };

    public static final Function<String, Double> DoubleFromString = new Function<String, Double>() {
        @Override
        public Double apply(String i) {
            return Double.valueOf(i);
        }
    };

    private final String key;

    XMLKeys(String value) {
        this.key = value;
    }

    /**
     * Checks if this enum is labelled by the given {@link String} <code>val</code>.
     *
     * @param val the {@link String} for which the enum is checked for equality.
     * @return <code>true</code> if <code>val</code> is equal to the value of this enum.
     */
    public boolean equals(String val) {
        return key.equals(val);
    }

    /**
     * Builds an {@link Element} of the given {@link Document} <code>doc</code>.
     *
     * The element is labelled with the {@link String} value of this enum.
     *
     * @param doc the {@link Document} for which the element is created
     * @return an {@link Element} labelled with this enum value.
     */
    public Element createElement(Document doc) {
        return doc.createElement(key);
    }

    /**
     * Creates an element for the given {@link Document} <code>doc</code> and adds it to the list of children of the
     * node <code>parent</code>.
     *
     * The created element is labelled with the {@link String} that labels this enum.
     *
     * @param doc the {@link Document} for which the element is created
     * @param parent the parent {@link Element} the created element.
     * @return an {@link Element} labelled with this enum value.
     */
    public Element createElement(Document doc, Element parent) {
        Element result = doc.createElement(key);
        parent.appendChild(result);
        return result;
    }

    /**
     * Checks if this enum labels the given {@link Element}.
     *
     * @param e the checked {@link Element}
     * @return <code>true</code> if the name of <code>e</code> is the value of this enum.
     */
    public boolean labelsElement (Element e) {
        return key.equals(e.getNodeName());
    }

    /**
     * Sets an attribute labelled by this enum with the given <code>value</code>.
     *
     * @param el the {@link Element} for which the attribute is set.
     * @param value of the attribute
     */
    public void setAttribute(Element el, String value) {
        el.setAttribute(key, value);
    }

    /**
     * Sets an attribute labelled by this enum.
     *
     * This generic method sets the attribute labelled by this enum with the given <code>value</code>. The type of
     * <code>value</code> must be supported by a <code>valueOf</code> method of {@link String}.
     *
     * @param el the {@link Element} for which the attribute is set.
     * @param value of the attribute
     * @param <T> the type of the value assigned to the attribute
     * @see String#valueOf
     */
    public <T> void setAttribute(Element el, T value) {
        setAttribute(el, String.valueOf(value));
    }

    /**
     * Sets an attribute if its value is not equal to a default one.
     *
     * @param el the {@link Element} for which the attribute is set.
     * @param value of the attribute
     * @param default_value the default value against which <code>value</code> is checked for equality.
     * @param <T> the type of the value assigned to the attribute
     */
    public <T> void setNotDefaultAttribute(Element el, T value, T default_value) {
        if (value != default_value && !value.equals(default_value)) {
            setAttribute(el, value);
        }
    }

    /**
     * Returns the value of the attribute labelled by this enum (if it exists).
     *
     * @param el the {@link Element}
     * @return the value of the attribute labelled by this enum of <code>null</code> if it does noe exists.
     */
    public String getValueFor(Element el) {
        return el.getAttribute(key);
    }

    /**
     * Checks if this enum is an attribute of the given {@link Element} <code>el</code>.
     *
     * @param el the checked {@link Element}
     * @return <code>true</code> if <code>el</code> has an attribute labelled with this enum.
     */
    public boolean isAttributeOf(Element el) {
        return el.hasAttribute(key);
    }

    /**
     * Gets the value of this attribute for the given {@link Element} if the attribute exists.
     *
     * If {@code element} has an attribute with the name of this enum its ({@link String}) value is returned. If the
     * attribute does not exists the {@code default_value} is returned.
     *
     * @param el the {@link Element} for which the attribute is read
     * @param default_value the default value returned if the attribute is not present
     * @return the value of this attribute.
     */
    public String getValueFor(Element el, String default_value) {
        if (isAttributeOf(el))
            return getValueFor(el);
        return default_value;
    }

    /**
     * Gets the value of this attribute as an {@link Integer}.
     *
     * If the element has this attribute its {@link String} value is converted into an {@link Integer} using
     * {@link Integer#valueOf}.
     *
     * @param el the {@link Element} for which the attribute is retrieved.
     * @param default_value the default value of the attribute if not present
     * @return the value of this attribute.
     */
    public Integer getValueFor(Element el, Integer default_value) {
        return getValueFor(el, IntegerFromString, default_value);
    }

    /**
     * Gets the value of this attribute as an {@link Integer}.
     *
     *  If this attribute does not exists a {@link NoSuchElementException} is raised.
     *
     * @param el the {@link Element} for which the attribute is retrieved.
     * @return the value of this attribute if present
     * @throws NoSuchElementException if this attribute does not exists for {@code el}.
     */
    public Integer getIntegerValueFor(Element el) throws NoSuchElementException {
        return getValueFor(el, IntegerFromString);
    }

    /**
     * Gets the value of this attribute as an {@link Double}.
     *
     * If the element has this attribute its {@link String} value is converted into an {@link Double} using
     * {@link Double#valueOf}.
     *
     * @param el the {@link Element} from which the attribute is retrieved.
     * @param default_value the value returned if this attribute is not present.
     * @return the value of this attribute if present or {@code default_value} if not.
     */
    public Double getValueFor(Element el, Double default_value) {
        return getValueFor(el, DoubleFromString, default_value);
    }

    /**
     * Gets the value of this attribute as an {@link Double}.
     *
     *  If this attribute does not exists a {@link NoSuchElementException} is raised.
     *
     * @param el the {@link Element} for which the attribute is retrieved.
     * @return the value of this attribute if present
     * @throws NoSuchElementException if this attribute does not exists for {@code el}.
     */
    public Double getDoubleValueFor(Element el) throws NoSuchElementException {
        return getValueFor(el, DoubleFromString);
    }

    /**
     * Gets the value of this attribute as an {@link Boolean}.
     *
     * If the element has this attribute its {@link String} value is converted into an {@link Boolean} using
     * {@link Boolean#valueOf}.
     *
     * @param el the {@link Element} from which the attribute is retrieved.
     * @param default_value the value returned if this attribute is not present.
     * @return the value of this attribute if present or {@code default_value} if not.
     */
    public Boolean getValueFor(Element el, Boolean default_value) {
        return getValueFor(el, BooleanFromString, default_value);
    }

    /**
     * Gets the value of this attribute converted to {@code R} type.
     *
     * If element {@code el} has this enum as attribute, its {@link String} value is translated into an {@code R}
     * object using the function {@code translate}. If the attribute does not exists {@code default_value} is returned.
     *
     * @param el the {@link Element} from which the attribute is retrieved.
     * @param translate a {@link Function} used to translate the {@link String} value of the attribute into {@code R}
     *                  type.
     * @param default_value the value returned if this attribute is not present.
     * @param <R> the class used to store the value of this attribute.
     * @return the value of this attribute converted using {@code translate}.
     */
    public <R> R getValueFor(Element el, Function<String, R> translate, R default_value) {
        if (isAttributeOf(el))
            return getValueFor(el, translate);
        return default_value;
    }

    /**
     * Gets the value of this attribute converted to {@code R} type.
     *
     * If element {@code el} has this enum as attribute, its {@link String} value is translated into an {@code R}
     * object using the function {@code translate}. If the attribute does not exists an exception is raised.
     *
     * @param el the {@link Element} from which the attribute is retrieved.
     * @param translate a {@link Function} used to translate the {@link String} value of the attribute into {@code R}
     *                  type.
     * @param <R> the class used to store the value of this attribute.
     * @return the value of this attribute converted using {@code translate}.
     * @throws NoSuchElementException if this attribute does not exists for {@code el}.
     */
    public <R> R getValueFor(Element el, Function<String, R> translate) throws NoSuchElementException {
        if (!isAttributeOf(el))
            throw new NoSuchElementException(key);
        return translate.apply(el.getAttribute(key));
    }
}
