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


# Filtrer les classes avec plus de 20 assertions
classes_plus_de_20_assertions <- data_jfreechart[data_jfreechart$TASSERT > 20, ]

# Afficher les classes
print(classes_plus_de_20_assertions)

# Filtrer les classes avec plus de 20 assertions
classes_moins_egale_a_20_assertions <- data_jfreechart[data_jfreechart$TASSERT <= 20, ]

# Afficher les classes
print(classes_moins_egale_a_20_assertions)

#Enregistrement fichier
subset_data <- subset(data_jfreechart, TASSERT <= 20  )
write.table(subset_data, file = "~/Downloads/IFT3913-main 3/Tp3/classes_tassert_inferieur_egale_20", sep = ",", row.names = FALSE)


#Enregistrement fichier
subset1_data <- subset(data_jfreechart, TASSERT > 20  )
write.table(subset1_data, file = "~/Downloads/IFT3913-main 3/Tp3/classes_tassert_sup_20", sep = ",", row.names = FALSE)

