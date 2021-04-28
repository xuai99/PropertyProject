package protect.myRentalInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class myRentalCalt
{
    static List<PropertyHousingCalculation> calculateForYears(PropertyInformation propertyInformation, int numYears)
    {
        List<PropertyHousingCalculation> list = new ArrayList<>(numYears);
        double interest=0.0;
        double getPrinciple=0.0,getTotal=0.0;
        double grossRent = propertyInformation.grossRent * 12;
        double otherIncome = propertyInformation.otherIncome*12;
        double myloanBalance = 0.0;
        double totalExpenses = grossRent * propertyInformation.expenses / 100.0;
        if(propertyInformation.expensesItemized.isEmpty() == false)
        {
            totalExpenses = myRentalCalt.sumMapItems(propertyInformation.expensesItemized) * 12;
        }

        double downPercent;
        if(propertyInformation.useLoan)
        {
            downPercent = ((double) propertyInformation.downPayment)/100.0;
        }
        else
        {
            downPercent = 1.0;
        }

        double mortgage = monthlyMortgagePayment(propertyInformation);
        double yearlyMortgage = mortgage*12;
        double downPayment = downPercent * (double) propertyInformation.purchasePrice;

        double loanBalance = propertyInformation.purchasePrice - downPayment;
        double principal =0.0;
        double showLoan = loanBalance;
        double myNetIncome =0.0;

        double propertyValue = propertyInformation.afterRepairsValue;
        double closing = propertyInformation.sellingCosts;

        double purchaseCost = propertyInformation.purchaseCosts* propertyInformation.purchasePrice/100.0;
        if(propertyInformation.purchaseCostsItemized.isEmpty() == false)
        {
            purchaseCost = myRentalCalt.sumMapItems(propertyInformation.purchaseCostsItemized);
        }

        double depreciation = (propertyInformation.purchasePrice - propertyInformation.landValue + purchaseCost) / 27.5;

        int repairRemodelCosts = propertyInformation.repairRemodelCosts;
        if(propertyInformation.repairRemodelCostsItemized.isEmpty() == false)
        {
            repairRemodelCosts = myRentalCalt.sumMapItems(propertyInformation.repairRemodelCostsItemized);
        }

        double totalCashNeeded = downPayment + purchaseCost + repairRemodelCosts;

        for(int year = 1; year <= numYears; year++)
        {
            PropertyHousingCalculation calc = new PropertyHousingCalculation();

            calc.grossRent = grossRent;
            grossRent = grossRent* (1 + propertyInformation.incomeIncrease/100.0);
            calc.otherIncome = otherIncome;
            otherIncome = otherIncome*(1+ propertyInformation.incomeIncrease/100.0);

            calc.vacancy = calc.grossRent * propertyInformation.vacancy / 100.0;

            calc.operatingIncome = calc.grossRent +calc.otherIncome  - calc.vacancy;

            calc.totalExpenses = totalExpenses;
            totalExpenses *= (1 + propertyInformation.expenseIncrease/100.0);

            calc.netOperatingIncome = calc.operatingIncome - calc.totalExpenses;






            calc.propertyValue = propertyValue;
            propertyValue *= (1 + propertyInformation.appreciation/100.0);

            for(int month = 1; month <= 12; month++)
            {
                interest = (showLoan * propertyInformation.interestRate/100.0);
                principal = yearlyMortgage - interest;

                principal = principal/12;

                calc.loanInterest += interest/12;
                getPrinciple += principal;
                showLoan = showLoan - principal;
                if(showLoan < 0  || showLoan ==00  )
                {
                    showLoan = 0;
                    yearlyMortgage = showLoan;

                }
            }
            if(calc.loanPayments > showLoan)
            {
                calc.loanPayments = showLoan;
            }

            if(showLoan == 0 || showLoan < 0 || mortgage > showLoan ){
                calc.loanPayments = 0;
                calc.cashFlow = calc.netOperatingIncome  ;
                calc.afterTaxCashFlow = calc.cashFlow * (1 - propertyInformation.incomeTaxRate/100.0);
            }else if(showLoan != 0 || showLoan > 0) {
                calc.loanPayments = yearlyMortgage;
                calc.cashFlow = calc.netOperatingIncome -calc.loanPayments ;
                calc.afterTaxCashFlow = calc.cashFlow * (1 - propertyInformation.incomeTaxRate/100.0);

            }

            myNetIncome = myNetIncome + calc.loanInterest;
            calc.calcInterest += myNetIncome;

            calc.calcPrinciple += getPrinciple;
            calc.TotalPI = calc.calcInterest+calc.calcPrinciple;


            calc.loanBalance = showLoan;
            calc.totalEquity = calc.propertyValue - showLoan;

            if(year <= 27)
            {
                calc.depreciation = depreciation;
            }
            else if(year == 28)
            {
                // On the last year, one only receives 1/2 of the normal value
                calc.depreciation = depreciation/2;
            }

            if(propertyInformation.purchasePrice > 0)
            {
                calc.capitalization = calc.netOperatingIncome * 100.0 / (double) propertyInformation.purchasePrice;
            }

            if(totalCashNeeded > 0)
            {
                calc.cashOnCash = calc.cashFlow * 100.0 / totalCashNeeded;
            }

            if(propertyInformation.purchasePrice > 0)
            {
                calc.rentToValue = calc.grossRent * 100.0 / propertyInformation.afterRepairsValue;
            }

            if(propertyInformation.grossRent > 0)
            {
                calc.grossRentMultiplier = calc.propertyValue / calc.grossRent;
            }

            list.add(year-1, calc);
        }

        return list;
    }

    static double monthlyMortgagePayment(PropertyInformation propertyInformation)
    {
        double downPercent;
        if(propertyInformation.useLoan)
        {
            downPercent = ((double) propertyInformation.downPayment)/100.0;
        }
        else
        {
            downPercent = 1.0;
        }
        double myFinancePercentage = 1.0 - downPercent;
        double financed = propertyInformation.purchasePrice * myFinancePercentage;
        double monthlyInterest = propertyInformation.interestRate / 100.0 / 12;
        int MonthylPayment = propertyInformation.loanDuration * 12;
        double PlusRateRaised = Math.pow(1 + monthlyInterest, MonthylPayment);
        double myMonthlymortgageValue = financed * (monthlyInterest * PlusRateRaised) / (PlusRateRaised - 1);

        return myMonthlymortgageValue;
    }


    static int sumMapItems(Map<String, Integer> map)
    {
        int value = 0;

        for(Map.Entry<String, Integer> entry : map.entrySet())
        {
            value += entry.getValue();
        }

        return value;
    }
}
