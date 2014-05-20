package com.rancard.util;

public class Money {
    public static short scale = 100;
    protected long value;

    public Money(double x) {
        this.value = Math.round(x * scale);
    }

    public Money() {
    }

    public Money(Money x) {
        this.value = x.value;
    }

    public Money set(Money rs) {
        this.value = rs.value;
        return this;
    }

    public Money set(double rs) {
        this.value = Math.round(rs * scale);
        return this;
    }

    public long wholeUnits() {
        return this.value / scale;
    }

    public short cents() {
        return (short) (int) (this.value * 100L / scale - 100L * wholeUnits());
    }

    public Money abs() {
        Money result = new Money(this);
        if (this.value < 0L) {
            result.value = (-result.value);
        }
        return result;
    }

    public short sign() {
        return (short) (this.value < 0L ? -1 : this.value > 0L ? 1 : 0);
    }

    public boolean equals(Money rs) {
        return this.value == rs.value;
    }

    public boolean lessThan(Money rs) {
        return this.value < rs.value;
    }

    public boolean greaterThan(Money rs) {
        return this.value > rs.value;
    }

    public boolean equals(double rs) {
        return this.value == rs * scale;
    }

    public boolean lessThan(double rs) {
        return this.value < rs * scale;
    }

    public boolean greaterThan(double rs) {
        return this.value > rs * scale;
    }

    public boolean equals(Object rs) {
        return ((rs instanceof Money)) && (((Money) rs).value == this.value);
    }

    public int compareTo(Object rs) {
        return sub((Money) rs).sign();
    }

    public int hashCode() {
        return new Long(this.value).hashCode();
    }

    public Money addSet(Money rs) {
        this.value += rs.value;
        return this;
    }

    public Money addSet(double rs) {
        this.value = (long)((this.value + rs * scale));
        return this;
    }

    public Money subSet(Money rs) {
        this.value -= rs.value;
        return this;
    }

    public Money subSet(double rs) {
        this.value = (long)((this.value - rs * scale));
        return this;
    }

    public Money mpySet(double rs) {
        this.value = (long)((this.value * rs));
        return this;
    }

    public Money divSet(double rs) {
        Math.round((float) (this.value = (long)(this.value / rs)));
        return this;
    }

    public Money minusSet() {
        this.value = (-this.value);
        return this;
    }

    public Money add(Money rs) {
        return new Money(this).addSet(rs);
    }

    public Money add(double rs) {
        return new Money(this).addSet(rs);
    }

    public Money sub(Money rs) {
        return new Money(this).subSet(rs);
    }

    public Money sub(double rs) {
        return new Money(this).subSet(rs);
    }

    public Money mpy(double rs) {
        return new Money(this).mpySet(rs);
    }

    public Money div(double rs) {
        return new Money(this).divSet(rs);
    }

    public double div(Money rs) {
        return Math.round((float) (this.value / rs.value));
    }

    public Money minus() {
        return new Money(this).minusSet();
    }

    public static String pfx_symbol = "";
    public static String sfx_symbol = "";
    public static char decimal_point = '.';
    public static char group_separator = ',';
    public static String unit_name = "cedi";
    public static String cent_name = "pesewa";

    private static short log10(long x) {
        short result = 0;
        for (short r = 0; x >= 10L; x /= 10L) {
            result = (short) (r + 1);
        }
        return result;
    }

    public String toString() {
        boolean negative = this.value < 0L;
        this.value = (negative ? -this.value : this.value);
        long whole = wholeUnits();
        short cents = cents();
        short rest = (short) (int) (this.value - (cents + 100L * whole) * scale / 100L);

        String result = negative ? "-" : "";
        result = result + pfx_symbol;


        long[] divisors = {1L, 1000L, 1000000L, 1000000000L, 1000000000000L, 1000000000000000L, 1000000000000000000L};


        int group_no = log10(whole) / 3;
        int group_val = (int) (whole / divisors[group_no]);

        result = result + group_val;
        while (group_no > 0) {
            result = result + group_separator;
            whole -= group_val * divisors[(group_no--)];
            group_val = (short) (int) (whole / divisors[group_no]);
            if (group_val < 100) {
                result = result + "0";
            }
            if (group_val < 10) {
                result = result + "0";
            }
            result = result + group_val;
        }
        result = result + decimal_point;
        if (cents < 10) {
            result = result + "0";
        }
        result = result + cents;
        if (rest > 0) {
            while ((rest = (short) (rest * 10)) < scale) {
            }
            result = result + rest / scale;
        }
        if (negative) {
            this.value = (-this.value);
        }
        return result + sfx_symbol;
    }
}
