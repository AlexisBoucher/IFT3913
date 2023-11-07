#setwd("/Users/margueritemireille/Downloads/Devoir 3 QLM")
#Importation des données
data_jfreechart <- read.csv("jfreechart-test-stats.csv" , sep = ",")

#Boite à moustaches (Variables quantitatives)

## Métrique TLOC
boxplot(data_jfreechart$TLOC,
        ylab = "TLOC",
        main = 'Boite à moustache selon la métrique TLOC',
        col = "blue",
        las = 1,
        sub = "Données: jfreechart",
        notch = TRUE )
summary(data_jfreechart$TLOC)  


## Métrique WMC
boxplot(data_jfreechart$WMC,
        ylab = "WMC",
        main = 'Boite à moustache selon la métrique WMC',
        col = "blue",
        las = 1,
        sub = "Données: jfreechart",
        notch = TRUE,
        ylim = c(7,70))

summary(data_jfreechart$WMC)      

## Métrique TASSERT
boxplot(data_jfreechart$TASSERT,
        ylab = "TASSERT",
        main = 'Boite à moustache selon la métrique TASSERT',
        col = "blue",
        las = 1,
        sub = "Données: jfreechart",
        notch = TRUE)

        

summary(data_jfreechart$TASSERT)     

#Histogramme
hist_TLOC <- hist(data_jfreechart$TLOC, main = "Histogramme de la métrique", ylab = "TLOC")
hist_WMC <- hist(data_jfreechart$WMC, main = "Histogramme de la métrique", ylab = "WMC")
hist_TASSERT <- hist(data_jfreechart$TASSERT, main = "Histogramme de la métrique", ylab = "TASSERT")
