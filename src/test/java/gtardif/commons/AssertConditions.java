/*
 * 
 *  * ========================================================= *
 *  *************************************************************
 *  *      _______. _______  _______ .___________.____    ____  *
 *  *     /       ||   ____||   ____||           |\   \  /   /  *
 *  *    |   (----`|  |__   |  |__   `---|  |----` \   \/   /   *
 *  *     \   \    |   __|  |   __|      |  |       \_    _/    *
 *  * .----)   |   |  |____ |  |____     |  |         |  |      *
 *  * |_______/    |_______||_______|    |__|         |__|      *
 *  *                                                           *
 *  *************************************************************
 *  * ========================================================= *
 *           _____  _   _      _   
 *     /\   |  __ \| \ | |    | |  
 *    /  \  | |  | |  \| | ___| |_ 
 *   / /\ \ | |  | | . ` |/ _ \ __|
 *  / ____ \| |__| | |\  |  __/ |_ 
 * /_/    \_\_____/|_| \_|\___|\__|
 * 
 * 
 * 
 * NOTICE:
 * 
 * ##################################################################################
 * #                                                                                #
 * # This file is part of the Seety project.                                        #
 * # All information contained herein is, remains the property of PagesJaunes Group #
 * # The intellectual and technical concepts contained herein are proprietary to    #
 * # PagesJaunes Group and may be covered by Patents, patents in process, and are   #
 * # protected by trade secret or copyright law.                                    #
 * #                                                                                #
 * # Dissemination of this information or reproduction of this material is strictly #
 * # forbidden unless prior written permission is obtained from PagesJaunes Group.  #
 * #                                                                                #
 * # All Right Reserved                                                             #
 * # Copyright (c) 2011 , PagesJaunes Group                                         #
 * #                                                                                #
 * ##################################################################################
 */
package gtardif.commons;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.fest.assertions.Condition;

public class AssertConditions {
	public static Condition<Object> reflectionEqualTo(final Object other) {
		return new Condition<Object>() {
			@Override
			public boolean matches(Object value) {
				return EqualsBuilder.reflectionEquals(value, other);
			}
		};
	}
}
