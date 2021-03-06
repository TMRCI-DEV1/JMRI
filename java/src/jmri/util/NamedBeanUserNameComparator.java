package jmri.util;

import jmri.NamedBean;

/**
 * Comparator for JMRI NamedBeans via their User Names.
 * <p>
 * If the User Names are both non-null and are not equal, uses the {@link AlphanumComparator},
 * otherwise uses the {@link NamedBeanComparator}.
 *
 * @param <B> supported type of NamedBean
 */
public class NamedBeanUserNameComparator<B extends NamedBean> implements java.util.Comparator<B> {

    public NamedBeanUserNameComparator() {
    }

    static final AlphanumComparator comparator = new AlphanumComparator();

    @Override
    public int compare(B n1, B n2) {
        String s1 = n1.getUserName();
        String s2 = n2.getUserName();

        // handle both usernames being null or empty
        if ((s1 == null || s1.isEmpty()) && (s2 == null || s2.isEmpty())) {
            return n1.compareTo(n2);
        }

        // if both have user names, compare those
        if (! (s1 == null || s1.isEmpty()) && ! (s2 == null || s2.isEmpty())) {
            return comparator.compare(s1, s2);
        }

        // now must have one with and one without
        if (s1 == null || s1.isEmpty()) {
            return +1; // system name always after n2 with user name
        } else {
             return -1; // user name always before n2 with only system name
        }
    }
}
