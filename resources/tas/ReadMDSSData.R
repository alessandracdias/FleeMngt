
# Read data
Paths <- read.csv(file="Paths.csv", header=TRUE, sep=";", strip.white = TRUE)

ini_Location = Paths[which(Paths$DEPARTURE_BASE == ""),]
ini_Location = ini_Location[,c('TAIL','ARRIVAL_BASE')]

Paths = Paths[-which(Paths$DEPARTURE_BASE == ""),]

Paths$DEPARTURE_BASE = droplevels(Paths$DEPARTURE_BASE,exclude = "")

Paths = Paths[-which(Paths$DEPARTURE_BASE == Paths$ARRIVAL_BASE),]

Paths$Fuel = 0

for(i in 1:nrow(Paths)){
  if(Paths$Fuel[i] == 0){
    
    fuel = runif(1, min=3, max=7);
    
    ind1 = which(Paths$DEPARTURE_BASE == Paths$DEPARTURE_BASE[i] 
                  & Paths$ARRIVAL_BASE == Paths$ARRIVAL_BASE[i])
    
    ind2 = which(Paths$DEPARTURE_BASE == Paths$ARRIVAL_BASE[i] 
                & Paths$ARRIVAL_BASE == Paths$DEPARTURE_BASE[i])
    
    Paths$Fuel[ind1] = fuel;
    Paths$Fuel[ind2] = fuel;
  }
}

lofFuel = aggregate(x = Paths[,c("Fuel")], by = list(Paths$TAIL), FUN = sum)

lofFuel$PATH = paste("PP99", 1:nrow(lofFuel), sep = "")

Paths = merge(lofFuel, Paths, by.x = "Group.1", by.y = "TAIL")
colnames(Paths)[1] = "TAIL"
Paths$x = NULL
Paths$NEW_TAIL = Paths$TAIL

ini_Location$perfFactor = 1
ini_Location$perfFactor[which(ini_Location$TAIL == 'AA111')] = 1.032046
ini_Location$perfFactor[which(ini_Location$TAIL == 'AA116')] = 1.032197
ini_Location$perfFactor[which(ini_Location$TAIL == 'AA1117')] = 1.093318
ini_Location$perfFactor[which(ini_Location$TAIL == 'AA1123')] = 1.032046
ini_Location$perfFactor[which(ini_Location$TAIL == 'AA1124')] = 1.093318



