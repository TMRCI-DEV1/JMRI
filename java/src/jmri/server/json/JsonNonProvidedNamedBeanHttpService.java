package jmri.server.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Locale;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import jmri.Manager;
import jmri.NamedBean;

/**
 * Abstract implementation of JsonHttpService with specific support for
 * {@link jmri.NamedBean} objects.
 * <p>
 * <strong>Note:</strong> if the extending class meets the requirements of
 * {@link jmri.server.json.JsonNamedBeanHttpService}, it is recommended to
 * extend that class instead.
 *
 * @author Randall Wood (C) 2016, 2019
 * @param <T> the type supported by this service
 */
public abstract class JsonNonProvidedNamedBeanHttpService<T extends NamedBean> extends JsonHttpService {

    public JsonNonProvidedNamedBeanHttpService(ObjectMapper mapper) {
        super(mapper);
    }

    /**
     * Respond to an HTTP GET request for a list of items of type.
     * <p>
     * This is called by the {@link jmri.web.servlet.json.JsonServlet} to handle
     * get requests for a type, but no name. Services that do not have named
     * objects, such as the {@link jmri.server.json.time.JsonTimeHttpService}
     * should respond to this with a list containing a single JSON object.
     * Services that can't return a list may throw a 400 Bad Request
     * JsonException in this case.
     *
     * @param manager the manager for the requested type
     * @param type    the type of the requested list
     * @param data    JSON object possibly containing filters to limit the list
     *                to
     * @param request the JSON request
     * @return a JSON list
     * @throws JsonException may be thrown by concrete implementations
     */
    @Nonnull
    protected final JsonNode doGetList(Manager<T> manager, String type, JsonNode data, JsonRequest request)
            throws JsonException {
        ArrayNode array = this.mapper.createArrayNode();
        for (T bean : manager.getNamedBeanSet()) {
            array.add(this.doGet(bean, bean.getSystemName(), type, request));
        }
        return message(array, request.id);
    }

    /**
     * Respond to an HTTP GET request for a list of items of type.
     * <p>
     * This is called by the {@link jmri.web.servlet.json.JsonServlet} to handle
     * get requests for a type, but no name. Services that do not have named
     * objects, such as the {@link jmri.server.json.time.JsonTimeHttpService}
     * should respond to this with a list containing a single JSON object.
     * Services that can't return a list may throw a 400 Bad Request
     * JsonException in this case.
     *
     * @param manager the manager for the requested type
     * @param type    the type of the requested list
     * @param data    JSON object possibly containing filters to limit the list
     *                to
     * @param locale  the requesting client's Locale
     * @param id      the message id set by the client
     * @return a JSON list
     * @throws JsonException may be thrown by concrete implementations
     */
    @Nonnull
    protected final JsonNode doGetList(Manager<T> manager, String type, JsonNode data, Locale locale, int id)
            throws JsonException {
        return doGetList(manager, type, data, new JsonRequest(locale, JSON.V5, JSON.GET, id));
    }

    /**
     * Respond to an HTTP GET request for the requested name.
     * <p>
     * If name is null, return a list of all objects for the given type, if
     * appropriate.
     * <p>
     * This method should throw a 500 Internal Server Error if type is not
     * recognized.
     *
     * @param bean    the requested object
     * @param name    the name of the requested object
     * @param type    the type of the requested object
     * @param request the JSON request
     * @return a JSON description of the requested object
     * @throws JsonException if the named object does not exist or other error
     *                       occurs
     */
    @Nonnull
    protected abstract ObjectNode doGet(T bean, @Nonnull String name, @Nonnull String type,
            @Nonnull JsonRequest request)
            throws JsonException;

    /**
     * Get the NamedBean matching name and type. If the request has a method
     * other than GET, this may modify or create the NamedBean requested. Note
     * that name or data may be null, but it is an error to have both be null.
     *
     * @param name    the name of the requested object
     * @param type    the type of the requested object
     * @param data    the JsonNode containing the JSON representation of the
     *                bean to get
     * @param request the JSON request
     * @return the matching NamedBean or null if there is no match
     * @throws JsonException            if the name is invalid for the type
     * @throws IllegalArgumentException if both name is null and data is empty
     */
    @CheckForNull
    protected abstract T getNamedBean(@Nonnull String type, @Nonnull String name, @Nonnull JsonNode data,
            @Nonnull JsonRequest request) throws JsonException;

    /**
     * Create the JsonNode for a {@link jmri.NamedBean} object.
     *
     * @param bean    the bean to create the node for
     * @param name    the name of the bean; used only if the bean is null
     * @param type    the JSON type of the bean
     * @param request the JSON request
     * @return a JSON node
     * @throws JsonException if the bean is null
     */
    @Nonnull
    public ObjectNode getNamedBean(T bean, @Nonnull String name, @Nonnull String type, @Nonnull JsonRequest request)
            throws JsonException {
        if (bean == null) {
            throw new JsonException(404, Bundle.getMessage(request.locale, JsonException.ERROR_NOT_FOUND, type, name),
                    request.id);
        }
        ObjectNode data = mapper.createObjectNode();
        data.put(JSON.NAME, bean.getSystemName());
        data.put(JSON.USERNAME, bean.getUserName());
        data.put(JSON.COMMENT, bean.getComment());
        ArrayNode properties = data.putArray(JSON.PROPERTIES);
        bean.getPropertyKeys().stream().forEach(key -> {
            Object value = bean.getProperty(key);
            if (value != null) {
                properties.add(mapper.createObjectNode().put(key, value.toString()));
            } else {
                properties.add(mapper.createObjectNode().putNull(key));
            }
        });
        return message(type, data, request.id);
    }

    /**
     * Handle the common elements of a NamedBean that can be changed in an POST
     * message.
     * <p>
     * <strong>Note:</strong> the system name of a NamedBean cannot be changed
     * using this method.
     *
     * @param bean    the bean to modify
     * @param data    the JsonNode containing the JSON representation of bean
     * @param name    the system name of the bean
     * @param type    the JSON type of the bean
     * @param request the JSON request
     * @return the bean so that this can be used in a method chain
     * @throws JsonException if the bean is null
     */
    @Nonnull
    protected T postNamedBean(T bean, @Nonnull JsonNode data, @Nonnull String name, @Nonnull String type,
            @Nonnull JsonRequest request) throws JsonException {
        if (bean == null) {
            throw new JsonException(404, Bundle.getMessage(request.locale, JsonException.ERROR_NOT_FOUND, type, name),
                    request.id);
        }
        if (data.path(JSON.USERNAME).isTextual()) {
            bean.setUserName(data.path(JSON.USERNAME).asText());
        }
        if (!data.path(JSON.COMMENT).isMissingNode()) {
            JsonNode comment = data.path(JSON.COMMENT);
            bean.setComment(comment.isTextual() ? comment.asText() : null);
        }
        return bean;
    }

}
