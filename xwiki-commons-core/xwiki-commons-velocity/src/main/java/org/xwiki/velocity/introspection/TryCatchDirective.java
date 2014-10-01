/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.velocity.introspection;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * Allows catching exception from Velocity. Example usage:
 * <pre>
 * {@code #try()
 *   ...
 *   #end
 *
 *   ## At this stage $exception contains the exception if any has been thrown.
 *   ## It can be tested like this:
 *   #if ("$!exception" != '')
 *     ## An exception has been thrown
 *   #end}
 * </pre>
 *
 * @version $Id$
 * @since 6.3M1
 */
public class TryCatchDirective extends Directive
{
    @Override
    public String getName()
    {
        return "try";
    }

    @Override
    public int getType()
    {
        return BLOCK;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node)
        throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException
    {
        try {
            // Make sure to clear any previous exception
            context.remove("exception");
            return node.jjtGetChild(0).render(context, writer);
        } catch (Exception e) {
            // Save the exception in the $exception velocity context variable
            context.put("exception", e);
            return true;
        }
    }
}
