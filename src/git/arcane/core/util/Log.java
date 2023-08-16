package git.arcane.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles a static collection of {@link Logger}s so we can log any situation conveniently from one class.
 */
public class Log {

    public static Logger CORE = LoggerFactory.getLogger("CORE");
    public static Logger GAME = LoggerFactory.getLogger("GAME");
    public static Logger RENDER = LoggerFactory.getLogger("RENDER");

}
