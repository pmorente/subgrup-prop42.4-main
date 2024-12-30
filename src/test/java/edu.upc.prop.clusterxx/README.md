Tests de integración:

En estos test de integración se comprueban uno por uno los casos de uso descritos en nuestra documentación, incluyendo los posibles resultados no favorables.
Para facilitar su lectura, las siguientes descripciones también están presentes en el archivo del test encima de cada test correspondiente.



testCreateProduct:
  Este test verifica que se pueda crear un producto o importarlo y todos sus posibles resultados.
  Objeto de la prueba: Crear Producto
  Ficheros necesarios: - (test_export_cjtProduct.csv creado en el test)
  Valores estudiados: 
     -Crear un producto con nombre, precio y descripción correctos
     -Crear un producto con nombre, precio y descripción incorrectos


testDeleteProduct:
  Este test verifica que se pueda eliminar un producto y todos sus posibles resultados.
  Objeto de la prueba: Eliminar Productos
  Ficheros necesarios: - 
  Valores estudiados: 
     -Eliminar un producto existente
     -Eliminar un producto inexistente


testModificarProducto:
  Este test verifica que se pueda cambiar la descripción de un producto y todos sus posibles resultados.
  Objeto de la prueba: Cambiar Descripción de Producto
  Ficheros necesarios: - 
  Valores estudiados: 
     -Modificar un producto existente
     -Modificar un producto inexistente

testConsultarProductos:
  Este test verifica que se pueda consultar productos y todos sus posibles resultados.
  Objeto de la prueba: Consultar Productos
  Ficheros necesarios: -
  Valores estudiados:
     -Consultar productos existentes
     -Consultar productos inexistentes


testCargarSimilitud:
   Este test verifica que se pueda crear la similitud entre dos productos y todos sus posibles resultados.
   Objeto de la prueba: Cargar Similitud
   Ficheros necesarios: - (test_export_Similitud.csv creado en el test)
   Valores estudiados:
     -Cargar similitud entre dos productos existentes
     -Cargar similitud entre dos productos inexistentes
     -Cargar similitud con coeficiente incorrecto

testConsultarSimilitud:
  Este test verifica que se pueda consultar la similitud entre dos productos y todos sus posibles resultados.
  Objeto de la prueba: Consultar Similitudes de Productos
  Ficheros necesarios: - (test_export_Similitud.csv creado en el test)
  Valores estudiados:
     -Consultar similitud entre dos productos existentes
     -Consultar similitud entre dos productos inexistentes
     -Consultar similitud entre productos sin existir ninguno
     -Exportar similitud entre productos existentes
     -Exportar similitud sin productos

testEliminarSimilitud:
  Este test verifica que se pueda eliminar la similitud entre dos productos y todos sus posibles resultados.
  Objeto de la prueba: Eliminar Similitud de Productos
  Ficheros necesarios: - 
  Valores estudiados:
     -Eliminar similitud entre dos productos existentes
     -Eliminar similitud entre dos productos inexistentes

testModificarSimilitud:
  Este test verifica que se pueda modificar la similitud entre dos productos y todos sus posibles resultados.
  Objeto de la prueba: Modificar Similitud de Productos
  Ficheros necesarios: -
  Valores estudiados:
    -Modificar similitud entre dos productos existentes
    -Modificar similitud entre dos productos inexistentes
    -Modificar similitud con coeficiente incorrecto

testCrearEstanteria:
  Este test verifica que se pueda crear una estantería y todos sus posibles resultados.
  Objeto de la prueba: Crear Estantería
  Ficheros necesarios: - 
  Valores estudiados:
    -Crear una estantería de tamaño correcto
    -Crear una estantería de tamaño incorrecto

testVerExportarEstanteria:
  Este test verifica que se pueda ver y exportar una estantería y todos sus posibles resultados.
  Objeto de la prueba: Ver y Exportar Estantería
  Ficheros necesarios: - (test_export_Shelve.csv creado en el test)
  Valores estudiados:
    -Ver una estantería
    -Ver una estantería sin productos
    -Exportar una estantería
    -Exportar una estantería sin productos

testModificarEstanteria:
  Este test verifica que se pueda modificar una estantería y todos sus posibles resultados.
  Objeto de la prueba: Modificar Estantería
  Ficheros necesarios: -
  Valores estudiados:
    -Modificar una estantería con productos correctos
    -Modificar una estantería con productos incorrectos
    -Añadir un producto a una estantería
    -Añadir un producto a una estantería sin espacios
    -Eliminar un producto de una estantería
    -Eliminar un producto de una estantería con productos incorrectos


testCalcularDistribucionProductos:
  Este test verifica que se pueda calcular la distribución de productos en una estantería y todos sus posibles resultados.
  Objeto de la prueba: Calcular Distribución de Productos
  Ficheros necesarios: -
  Valores estudiados:
    -Calcular la distribución de productos en una estantería
    -Calcular la distribución de productos en una estanteria sin productos
    -Calcular la distribución de productos en una estantería con productos incorrectos
    -Calcular la distribución de productos en una estantería con productos sin similitudes

testConsultarEstadisticas:
  Este test verifica que se pueda obtener las estadísticas de una estantería y todos sus posibles resultados.
  Objeto de la prueba: Consultar Estadísticas
  Ficheros necesarios: -
  Valores estudiados:
   -Consultar las estadísticas de una estantería
   -Consultar las estadísticas de una estantería no inicializada
   -Consultar las estadísticas de una estantería no calculada

testcrearUsuario:
  Este test verifica si se puede crear un usuario y todos sus posibles resultados.
  Objeto de la prueba: Crear usuario
  Ficheros necesarios: - 
  Valores estudiados:
   -Crear un usuario
   -Crear un usuario con nombre existente

testeliminarUsuario:
  Este test verifica si se puede eliminar un usuario y todos sus posibles resultados.
  Objeto de la prueba: Eliminar usuario
  Ficheros necesarios: - 
  Valores estudiados:
   -Eliminar un usuario
   -Eliminar un usuario inexistente

testlogin:
  Este test verifica si se puede hacer login y todos sus posibles resultados.
  Objeto de la prueba: Login
  Ficheros necesarios: - 
  Valores estudiados:
   -Hacer login
   -Hacer login con usuario inexistente

testAutoguardado:
  Este test verifica que el autoguardado cumple con la lógica de estados esperada
  Objeto de la prueba: Autoguardado
  Ficheros necesarios: - (test_export_Sesion.csv creado en el test)
  Valores estudiados:
   -Cargar una sesión
   -Cargar una sesión inexistente

testGuardarSesion:
  Este test verifica que se pueda guardar una sesión y todos sus posibles resultados.
  Objeto de la prueba: Guardar Sesión
  Ficheros necesarios: - (test_export_Sesion.csv creado en el test)
  Valores estudiados:
   -Guardar una sesión
   -Guardar una sesión sin productos, estantería, similitudes y estadísticas

testcrearSesion:
  Este test verifica si se puede crear una sesión y todos sus posibles resultados.
  Objeto de la prueba: Crear Sesión
  Ficheros necesarios: - (test_export_Sesion.csv creado en el test)
  Valores estudiados:
   -Crear una sesión desde un fichero
   -Crear una sesión desde un usuario
   -Crear una sesión desde fichero inexistente
   -Crear una sesión desde usuario inexistente

