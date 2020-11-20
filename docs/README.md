[![Kotlin Version](https://img.shields.io/badge/kotlin-1.3.50-blue.svg)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![CodeFactor](https://www.codefactor.io/repository/github/diogobernardino/williamchart/badge)](https://www.codefactor.io/repository/github/diogobernardino/williamchart)

Williamchart is an Android Library to rapidly implement attractive and insightful charts in android applications.

### Gradle 

``` groovy
implementation 'com.diogobernardino:williamchart:3.2.0'
```

If you find this library useful and decide to use it in your projects please drop me a line [@dfbernardino][1], I will be happy to know about it.

### Usage

#### All charts

```xml
<com.db.williamchart.view.chart_view
  ...
  app:chart_labelsColor="color"
  app:chart_labelsSize="dimension"
  app:chart_labelsFont="font" 
/>
```

#### Line Chart

```xml
<com.db.williamchart.view.LinechartView
  ...
  app:chart_lineColor="color"
  app:chart_lineThickness="dimension"
  app:chart_smoothLine=[ "true" | "false" ]
  app:chart_pointDrawable="drawable" 
/>
```

#### Bar Chart

```xml
<com.db.williamchart.view.BarChartView|HorizontalBarChartView
  ...
  app:chart_spacing="dimension"
  app:chart_barsColor="color"
  app:chart_barsBackgroundColor="color"
  app:chart_barsRadius="dimension" 
/>
```

#### Donut Chart

```xml
<com.db.williamchart.view.DonutChartView
  ...
  app:chart_donutThickness="dimension"
  app:chart_donutColor="color"
  app:chart_donutBackgroundColor="color"
  app:chart_donutRoundCorners="boolean"
  app:chart_donutTotal="float"
/>
```


License
-------

    Copyright 2019 Diogo Bernardino

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://twitter.com/dfbernardino
