/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies an expression written in native SQL that is used to read the value of
 * an attribute instead of storing the value in a {@link jakarta.persistence.Column}.
 * <p>
 * A {@code Formula} mapping defines a "derived" attribute, whose state is determined
 * from other columns and functions when an entity is read from the database.
 * <p>
 * A formula may involve multiple columns and SQL operators:
 * <pre>
 * // perform calculations using SQL operators
 * &#64;Formula("sub_total + (sub_total * tax)")
 * long getTotalCost() { ... }
 * </pre>
 * <p>
 * It may even call SQL functions:
 * <pre>
 * // call native SQL functions
 * &#64;Formula("upper(substring(middle_name from 0 for 1))")
 * Character getMiddleInitial() { ... }
 * </pre>
 * <p>
 * For an entity with {@linkplain jakarta.persistence.SecondaryTable secondary tables},
 * a formula may involve columns of the primary table, or columns of any one of the
 * secondary tables. But it may not involve columns of more than one table.
 * <p>
 * The {@link ColumnTransformer} annotation is an alternative in certain cases, allowing
 * the use of native SQL to read <em>and write</em> values to a column.
 * <pre>
 * // it might be better to use &#64;ColumnTransformer in this case
 * &#064;Formula("decrypt(credit_card_num)")
 * String getCreditCardNumber() { ... }
 * </pre>
 *
 * @see ColumnTransformer
 * @see DiscriminatorFormula
 * @see JoinFormula
 *
 * @author Emmanuel Bernard
 * @author Steve Ebersole
 *
 * @see DialectOverride.Formula
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Formula {
	/**
	 * The formula, written in native SQL.
	 */
	String value();
}
