# conventional way to import pandas
import pandas as pd
import numpy as np
from datetime import datetime
from xml.dom.minidom import parse, parseString
from xml.dom.minidom import getDOMImplementation
from xml.dom import minidom, Node
import random as random

# Returns a DataFrame
# df = pd.read_excel('~/git/FleetMngt/resources/data/FlightLegsKLNovo.xls', 
# df = pd.read_excel('FlightLegs.xls')
df = pd.read_excel('/Users/ivopdm/Documents/GitHub/FleeMngt/resources/data/FlightLegsRepublicNovo.xls')
                   
df['LegETD'] = df['LegETD'].apply(lambda x: x.strftime('%d/%m/%Y %H:%M:%S'))
df['LegETA'] = df['LegETA'].apply(lambda x: x.strftime('%d/%m/%Y %H:%M:%S'))



doc = minidom.Document()
flights = doc.createElement('flights')
doc.appendChild(flights)

for x in  df['Acft'].unique():
    router = doc.createElement('route')
    flights.appendChild(router)    
    legs= df[df.Acft == x]
    
    for index, row in legs.iterrows():
        flight = doc.createElement('flight')
        router.appendChild(flight)
        
#        flight.setAttribute('id', str(row['LegID']))    
        flight.setAttribute('id', str(row['FltNbr']))   

        
        flightID = doc.createElement('flightID')  
        flightID.appendChild(doc.createTextNode(str(row['FltNbr'])))
        flight.appendChild(flightID)
        
        origin = doc.createElement('origin')
        origin.appendChild(doc.createTextNode(str(row['LegOrig'])))
        flight.appendChild(origin)
        
        destination = doc.createElement('destination')
        destination.appendChild(doc.createTextNode(str(row['LegDest'])))
        flight.appendChild(destination)
        
        departureTime = doc.createElement('departureTime')
        departureTime.appendChild(doc.createTextNode(str(row['LegETD'])))
        flight.appendChild(departureTime)
        
        arrivalTime = doc.createElement('arrivalTime')
        arrivalTime.appendChild(doc.createTextNode(str(row['LegETA'])))
        flight.appendChild(arrivalTime)
        
        
        flightValue = doc.createElement('flightValue')
        value = random.randrange(6) * 1000 + 5000
        flightValue.appendChild(doc.createTextNode(str(value)))
        flight.appendChild(flightValue)
        
        fuel = doc.createElement('fuel')
 #       fuel.appendChild(doc.createTextNode(str(row['Fuel'])))
        tripFuel = random.randrange(6) * 1000 + 1000
        fuel.appendChild(doc.createTextNode(str(tripFuel)))
        flight.appendChild(fuel)
        
print(doc.toprettyxml(indent = '   '))

doc.writexml( open('data.xml', 'w'),
               indent="  ",
               addindent="  ",
               newl='\n')
 
doc.unlink()
