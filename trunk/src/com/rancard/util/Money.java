package com.rancard.util;
public class Money    //// implements java.lang,Comparable
{
  //%TITLE    Money Class            (copyright 1996, Information Disciplines, Inc)
//   Internal representation:   Always an integer, scaled so that unity
//   -----------------------      is the smallest measurable quantity

public static short scale=100;//  Smallest fraction of monetary unit to
                              //    be represented (e.g. 100: cents,
                              //    200: 8ths, 1000: mills);
                              //  User can override before creating
                              //    any Money objects

protected long  value;        //  Amount in monetary units * scale
//%SPACE 3
//   Constructors and assignment operations
//   --------------------------------------

// To support literal constants as well as simple input, we allow
// conversion from floating point, just as in the constructor.

 public Money(final double x) {value = Math.round(x * scale);}
 public Money()         {}      //  Default constructor, for efficiency
 public Money(final Money x)  {value = x.value;}
//%SPACE 3
//  Pseudo assignment operators
//  ---------------------------

 public Money set(final Money  rs)
        {value=         rs.value;       return this;}
 public Money set(final double rs)
        {value= Math.round(rs * scale); return this;}
//%SPACE 3
//  Accessors
//  ---------

public long  wholeUnits() {return value / scale;}

public short cents     () {return (short)((value*100)/scale
                                           - (100 * wholeUnits()));}
//%EJECT
//  Numeric utility functions
//  -------------------------

public Money abs()
       {Money  result = new Money(this);
        if (value < 0) result.value = - result.value;
        return result;
       }

public short sign()
       {return (short)(value > 0 ?  1
                    :  value < 0 ? -1 : 0);}

//%SPACE 3
//  Relational (predicate) operations:
//  ---------------------------------

 public boolean equals      (final Money  rs) {return value == rs.value;}
 public boolean lessThan    (final Money  rs) {return value <  rs.value;}
 public boolean greaterThan (final Money  rs) {return value >  rs.value;}

 public boolean equals      (final double rs) {return value == rs * scale;}
 public boolean lessThan    (final double rs) {return value <  rs * scale;}
 public boolean greaterThan (final double rs) {return value >  rs * scale;}
//%SPACE 3
//  These three functions are to comply with Java library conventions.
//  They are rarely needed in manipulating Money objects.

 public boolean equals        (final Object rs)
                {return rs instanceof Money && ((Money)rs).value == value;}

 public int compareTo         (final Object rs)
                {return sub((Money)rs).sign();}

 public int hashCode          ()
                {return (new Long(value)).hashCode();}
//%EJECT
//  Arithmetic operations:
//  ---------------------

//  For efficiency we follow Scott Myers ("More Effective C++", Addison
//  Wesley) in defining the compound assignment operators as primitive,
//  and then defining most ordinary arithmetic operators in terms of them.
//  Note that the compound assignment operators are much more
//  efficient, since no new object is created.

 public Money  addSet (final Money  rs)
                {value += rs.value;     return this;}
 public Money  addSet (final double rs)
                {value += rs * scale;   return this;}
 public Money  subSet (final Money  rs)
                {value -= rs.value;     return this;}
 public Money  subSet (final double rs)
                {value -= rs * scale;   return this;}
 public Money  mpySet (final double rs)
                {value *= rs;           return this;}
 public Money  divSet (final double rs)
                {Math.round(value/=rs); return this;}

 public Money  minusSet()
                {value = - value;       return this;}

 public Money  add (final Money  rs)
                {return new Money(this).addSet(rs);}
 public Money  add (final double rs)
                {return new Money(this).addSet(rs);}
 public Money  sub (final Money  rs)
                {return new Money(this).subSet(rs);}
 public Money  sub (final double rs)
                {return new Money(this).subSet(rs);}
 public Money  mpy (final double rs)
                {return new Money(this).mpySet(rs);}
 public Money  div (final double rs)
                {return new Money(this).divSet(rs);}
 public double div (final Money  rs)
                {return Math.round(value/rs.value);}

 public Money  minus()
                {return new Money(this).minusSet();}
//%EJECT
//   External representation: Constants used in output and input functions
//   -----------------------  (User may override)

public static String pfx_symbol     = "";     // Leading currency symbol
public static String sfx_symbol     = "";      // Trailing currency symbol
public static char  decimal_point   = '.';     // Character for fractions
public static char  group_separator = ',';     // Character for 1000nds
public static String unit_name = "cedi";     // Name of monetary unit
public static String cent_name = "pesewa";       // Name of fraction unit
//%SPACE 3
//  Conversion functions
//  --------------------

//  The following function is needed by toString (below) in order to
//  develop its result in left-to-right order:

  private static short log10(long x) //  This function returns the number
  {short result;                     //    of decimal digits in an integer
   for (result=0; x>=10; result++, x/=10); // Decimal "shift" and count
   return result;
  }
 //%SPACE 2
//  Conversion to string
//  --------------------

//  The result (external Money representation) is the concatenation of:
//  -   a leading minus sign, if needed
//  -   the prefix currency symbol, if any (U.S.:  '$')
//  -   groups of 3-digits separated by the group_separator (English: comma)
//           (no leading zeros on the leftmost group)
//  -   a decimal point (English: period)
//  -   the fractional portion, normally two digits, more if needed
//           and if scale > 100
//  -   the suffix currency symbol, if any (U.S.:  none)

 public String toString ()

 {boolean negative = (value < 0);        //  Remember original sign
  value = negative ? -value : value;     //  Discard sign temporarily
  long    whole = wholeUnits();          //  Separate arg. into whole
  short   cents = cents();               //    and fractional monetary units
  short   rest  = (short)(value - (cents + 100 * whole) * scale / 100);

  String result = (negative ? "-" : ""); //  Insert prefix minus, if needed
  result = result + pfx_symbol;          //  Insert dollar sign or equiv.

//  (function continued on next page)
//%EJECT
  //  Append groups of 3 digits separated by punctuation

  long   divisors[] = {        1,       1000,     1000000,
                       (long)1E9, (long)1E12,(long)1E15, (long)1E18};

  int    group_no  = log10(whole) / 3;
  int    group_val = (int)(whole  / divisors[group_no]);

  result = result + group_val;            // Append leftmost 3-digits
                                          //   without leading 0's

  while (group_no > 0)                    // For each remaining 3-digit group
   {result    = result + group_separator;         // Insert punctuation
    whole    -= group_val * divisors[group_no--]; // Compute new remainder
    group_val = (short)(whole/divisors[group_no]);// Get next 3-digit value
    if   (group_val < 100) result = result + "0"; // Insert embedded 0's
    if   (group_val <  10) result = result + "0"; //   as needed
    result = result + group_val;                  // Append group value
   }

//  Append the fractional portion

  result = result + decimal_point;   //    Append decimal point
  if (cents < 10)                    //    Insert leading 0 if needed
       result = result + "0";
  result = result + cents;           //    Append cents value

  if (rest > 0)                      //    Test for fractional pennies
     {while ((rest *= 10) < scale);  //    (rest *= power(10,log10(scale)))
      result = result + (rest/scale);//    Append fractional pennies, if any
     }
  if (negative) value = - value;     //    Restore original sign
  return result + sfx_symbol;        //    Append final symbol, if any

}     //    *******  End of toString function




}
