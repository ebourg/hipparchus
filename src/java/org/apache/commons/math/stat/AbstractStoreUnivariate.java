/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.commons.math.stat;
import java.util.Arrays;

/**
 * Provides univariate measures for an array of doubles.  
 * 
 * @author <a href="mailto:tobrien@apache.org">Tim O'Brien</a>
 */
public abstract class AbstractStoreUnivariate implements StoreUnivariate {

    /**
     * Returns the skewness of this collection of values
     * @see org.apache.commons.math.stat.StoreUnivariate#getSkewness()
     */
    public double getSkewness() {
        // Initialize the skewness
        double skewness = Double.NaN;

        // Get the mean and the standard deviation
        double mean = getMean();
        double stdDev = getStandardDeviation();

        // Sum the cubes of the distance from the mean divided by the 
        // standard deviation
        double accum = 0.0;
        for (int i = 0; i < getN(); i++) {
            accum += Math.pow((getElement(i) - mean) / stdDev, 3.0);
        }

        // Get N
        double n = getN();

        // Calculate skewness
        skewness = (n / ((n - 1) * (n - 2))) * accum;

        return skewness;
    }

    /**
     * Returns the kurtosis for this collection of values
     * @see org.apache.commons.math.stat.StoreUnivariate#getKurtosis()
     */
    public double getKurtosis() {
        // Initialize the kurtosis
        double kurtosis = Double.NaN;

        // Get the mean and the standard deviation
        double mean = getMean();
        double stdDev = getStandardDeviation();

        // Sum the ^4 of the distance from the mean divided by the 
        // standard deviation
        double accum = 0.0;
        for (int i = 0; i < getN(); i++) {
            accum += Math.pow((getElement(i) - mean) / stdDev, 4.0);
        }

        // Get N
        double n = getN();

        double coefficientOne = (n * (n + 1)) / ((n - 1) * (n - 2) * (n - 3));
        double termTwo = ((3 * Math.pow(n - 1, 2.0)) / ((n - 2) * (n - 3)));
        // Calculate kurtosis
        kurtosis = (coefficientOne * accum) - termTwo;

        return kurtosis;
    }

    /**
     * Returns the type or class of kurtosis that this collection of 
     * values exhibits
     * @see org.apache.commons.math.stat.StoreUnivariate#getKurtosisClass()
     */
    public int getKurtosisClass() {

        int kClass = StoreUnivariate.MESOKURTIC;

        double kurtosis = getKurtosis();
        if (kurtosis > 0) {
            kClass = StoreUnivariate.LEPTOKURTIC;
        } else if (kurtosis < 0) {
            kClass = StoreUnivariate.PLATYKURTIC;
        }

        return (kClass);

    }

    /**
     * Returns the mean for this collection of values
     * @see org.apache.commons.math.stat.Univariate#getMean()
     */
    public double getMean() {
        double arithMean = getSum() / getN();
        return arithMean;
    }

    /**
     * Returns the geometric mean for this collection of values
     * @see org.apache.commons.math.stat.Univariate#getGeometricMean()
     */
    public double getGeometricMean() {
        double gMean = Double.NaN;

        if (getN() > 0) {
            double sumLog = 0.0;
            for (int i = 0; i < getN(); i++) {
                sumLog += Math.log(getElement(i));
            }
            gMean = Math.exp(sumLog / (double)getN() );
        }

        return gMean;
    }

    /**
     * Returns the variance for this collection of values
     * @see org.apache.commons.math.stat.Univariate#getVariance()
     */
    public double getVariance() {
        // Initialize variance
        double variance = Double.NaN;

        if (getN() == 1) {
            // If this is a single value
            variance = 0;
        } else if (getN() > 1) {
            // Get the mean
            double mean = getMean();

            // Calculate the sum of the squares of the distance between each 
            // value and the mean
            double accum = 0.0;
            for (int i = 0; i < getN(); i++) {
                accum += Math.pow((getElement(i) - mean), 2.0);
            }

            // Divide the accumulator by N - Hmmm... unbiased or biased?
            variance = accum / (getN() - 1);
        }

        return variance;
    }

    /**
     * Returns the standard deviation for this collection of values
     * @see org.apache.commons.math.stat.Univariate#getStandardDeviation()
     */
    public double getStandardDeviation() {
        double stdDev = Double.NaN;
        if (getN() != 0) {
            stdDev = Math.sqrt(getVariance());
        }
        return (stdDev);
    }

    /**
     * Returns the maximum value contained herein.
     * @see org.apache.commons.math.stat.Univariate#getMax()
     */
    public double getMax() {

        // Initialize maximum to NaN
        double max = Double.NaN;

        for (int i = 0; i < getN(); i++) {
            if (i == 0) {
                max = getElement(i);
            } else {
                if (getElement(i) > max) {
                    max = getElement(i);
                }
            }
        }

        return max;
    }

    /**
     * Returns the minimum value contained herein
     * @see org.apache.commons.math.stat.Univariate#getMin()
     */
    public double getMin() {
        // Initialize minimum to NaN
        double min = Double.NaN;

        for (int i = 0; i < getN(); i++) {
            if (i == 0) {
                min = getElement(i);
            } else {
                if (getElement(i) < min) {
                    min = getElement(i);
                }
            }
        }

        return min;
    }

    /**
     * Returns the sum of all values contained herein
     * @see org.apache.commons.math.stat.Univariate#getSum()
     */
    public double getSum() {
        double accum = 0.0;
        for (int i = 0; i < getN(); i++) {
            accum += getElement(i);
        }
        return accum;
    }

    /**
     * Returns the sun of the squares of all values contained herein
     * @see org.apache.commons.math.stat.Univariate#getSumsq()
     */
    public double getSumsq() {
        double accum = 0.0;
        for (int i = 0; i < getN(); i++) {
            accum += Math.pow(getElement(i), 2.0);
        }
        return accum;
    }

    /**
     * @see org.apache.commons.math.stat.StoreUnivariate#getSortedValues()
     *
     */
    public double[] getSortedValues() {
        double[] values = getValues();
        Arrays.sort(values);
        return values;
    }

    /**
     * Returns an estimate for the pth percentile of the stored values
     * @see org.apache.commons.math.stat.StoreUnivariate#getPercentile(double)
     */
    public double getPercentile(double p) {
        if ((p > 100) || (p <= 0)) {
            throw new IllegalArgumentException("invalid percentile value");
        }
        double n = (double) getN();
        if (n == 0) {
            return Double.NaN;
        }
        if (n == 1) {
            return getElement(0); // always return single value for n = 1
        }
        double pos = p * (n + 1) / 100;
        double fpos = Math.floor(pos);
        int intPos = (int) fpos;
        double d = pos - fpos;
        double[] sorted = getSortedValues();
        if (pos < 1) {
            return sorted[0];
        }
        if (pos > n) {
            return sorted[getN() - 1];
        }
        double lower = sorted[intPos - 1];
        double upper = sorted[intPos];
        return lower + d * (upper - lower);
    }

}
