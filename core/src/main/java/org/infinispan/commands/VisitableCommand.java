package org.infinispan.commands;

import org.infinispan.commands.write.InvalidateCommand;
import org.infinispan.context.InvocationContext;
import org.infinispan.lifecycle.ComponentStatus;


/**
 * A type of command that can accept {@link Visitor}s, such as {@link org.infinispan.interceptors.DDAsyncInterceptor}.
 *
 * @author Manik Surtani (<a href="mailto:manik@jboss.org">manik@jboss.org</a>)
 * @since 4.0
 */
public interface VisitableCommand extends ReplicableCommand {
   /**
    * Performs the primary function of the command.  Please see specific implementation classes for details on what is
    * performed as well as return types. <b>Important</b>: this method will be invoked at the end of interceptors chain.
    * It should never be called directly from a custom interceptor.
    *
    * @param ctx invocation context
    * @return arbitrary return value generated by performing this command
    * @throws Throwable in the event of problems.
    */
   @Override
   Object perform(InvocationContext ctx) throws Throwable;

   /**
    * Accept a visitor, and return the result of accepting this visitor.
    *
    * @param ctx     invocation context
    * @param visitor visitor to accept
    * @return arbitrary return value
    * @throws Throwable in the event of problems
    */
   Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable;

   /**
    * Used by the InboundInvocationHandler to determine whether the command should be invoked or not.
    *
    * @return true if the command should be invoked, false otherwise.
    * @deprecated Since 9.0, no longer used.
    */
   @Deprecated
   default boolean shouldInvoke(InvocationContext ctx) {
      return true;
   }

   /**
    * Used to determine whether the command should be invoked or not.
    * Commands can opt to be discarded in case the cache status is not suited (as {@link InvalidateCommand})
    *
    * @return true if the command should NOT be invoked.
    * @deprecated Since 9.0, no longer used.
    */
   @Deprecated
   default boolean ignoreCommandOnStatus(ComponentStatus status) {
      return false;
   }

   /**
    * @return Nodes on which the command needs to read the previous values of the keys it acts on.
    * @throws UnsupportedOperationException if the distinction does not make any sense.
    */
   LoadType loadType();

   enum LoadType {
      /**
       * Never load previous value.
       */
      DONT_LOAD,
      /**
       * In non-transactional cache, load previous value only on the primary owner.
       * In transactional cache, the value is fetched to originator. Primary then does not have to
       * load the value but for write-skew check.
       */
      PRIMARY,
      /**
       * In non-transactional cache, load previous value on both primary and backups.
       * In transactional cache, the value is both fetched to originator and all owners have to load
       * it because it is needed to produce the new value.
       */
      OWNER
   }
}
