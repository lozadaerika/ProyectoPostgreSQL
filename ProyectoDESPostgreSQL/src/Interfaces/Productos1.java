
/*
 * Productos.java
 *
 * Created on 11-jul-2015, 19:37:47
 */
package Interfaces;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Erika
 */
public class Productos1 extends javax.swing.JFrame {
    DefaultTableModel modelo;

    /** Creates new form Productos */
    public Productos1() {
        initComponents();
        consultarProductos("");
        llenarTipo();
        jtbProductos.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        @Override
        public void valueChanged(ListSelectionEvent evt){
            if(jtbProductos.getSelectedRow()!=-1){
        txtCodigo.setText(jtbProductos.getValueAt(jtbProductos.getSelectedRow(), 0).toString());
        txtNombre.setText(jtbProductos.getValueAt(jtbProductos.getSelectedRow(), 1).toString());
        txtMarca.setText(jtbProductos.getValueAt(jtbProductos.getSelectedRow(), 2).toString());
        txtPrecio.setText(jtbProductos.getValueAt(jtbProductos.getSelectedRow(), 3).toString());
        txtStock.setText(jtbProductos.getValueAt(jtbProductos.getSelectedRow(), 4).toString());
        txtObservacion.setText(jtbProductos.getValueAt(jtbProductos.getSelectedRow(), 5).toString());
          jcbTipo.setSelectedItem(jtbProductos.getValueAt(jtbProductos.getSelectedRow(), 6).toString());
        txtFoto.setText(jtbProductos.getValueAt(jtbProductos.getSelectedRow(), 7).toString());
        if(txtFoto.getText().equals("Sin Informacion")||txtFoto.getText().isEmpty()){
            cargarImagenDefecto();
        }else
        cargarImagen(txtFoto.getText());
            }
        }
        }
        );
        
        
        
    }
    public void cargarImagen(String Path){
        ImageIcon im= new ImageIcon(Path);
        ImageIcon im2= new ImageIcon(im.getImage().getScaledInstance(jLabel11.getWidth(), jLabel11.getHeight(), 1));
        jLabel11.setIcon(im2);
    }
    
     public void cargarImagenDefecto(){
        ImageIcon im= new ImageIcon("C://FOTOS//CarroPrototipo.jpg");
        ImageIcon im2= new ImageIcon(im.getImage().getScaledInstance(jLabel11.getWidth(), jLabel11.getHeight(), 1));
        jLabel11.setIcon(im2);
    }

    
    
    public void limpiar(){
        txtCodigo.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtFoto.setText("");
        txtObservacion.setText("");
        jcbTipo.setSelectedItem("Seleccione...");
        
    }
    
    
    public void llenarTipo(){
        Conexion cc=new Conexion();
        Connection cn=cc.conectar();
        String sql="";
        sql="SELECT * FROM TIPO_PRODUCTO";
          jcbTipo.addItem("Seleccione...");
        try {
            Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
            jcbTipo.addItem(rs.getString("COD_TIP")+"-"+rs.getString("DES_TIP"));
        }
        } catch (Exception ex) {
             JOptionPane.showMessageDialog(null, "Error de consulta de Tipo Productos"+ex);
        } 
    }
    
    public void consultarProductos(String codigo){
        String titulos[]={"Código","Nombre","Marca","Precio","Stock","Observación","Tipo","Foto"};
        String [] registros= new String[8];
        modelo=new DefaultTableModel(null, titulos);
        jtbProductos.setModel(modelo);
        Conexion cc= new Conexion();
        Connection cn=cc.conectar();
         Connection cn2=cc.conectar();
        String sql="",sql2="";
        sql="SELECT * FROM public.PRODUCTO WHERE COD_PRO LIKE '%"+codigo+"%'";
        try {
            Statement psd=cn.createStatement();
            ResultSet rs= psd.executeQuery(sql);
            while(rs.next()){
                registros[0]=rs.getString("COD_PRO");
                registros[1]=rs.getString("NOM_PRO");
                registros[2]=rs.getString("MAR_PRO");
                registros[3]=rs.getString("PRE_PRO");
                registros[4]=rs.getString("STOCK");
                registros[5]=rs.getString("OBS_PRO");
                String tipo=rs.getString("COD_TIPO_PRO");
                sql2="SELECT DES_TIP FROM public.TIPO_PRODUCTO WHERE COD_TIPO_PRO='"+tipo+"'";
                 Statement psd2=cn2.createStatement();
                ResultSet rs2= psd2.executeQuery(sql2);
                while(rs2.next()){
                    registros[6]=tipo+"-"+rs2.getString("NOM_TIPO_PRO");
                }
                registros[7]=rs.getString("FOTO");
                modelo.addRow(registros);
            }
            jtbProductos.setModel(modelo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error de consulta de Productos"+ex);
        } 
    }
    public void busqueda(){
        JFileChooser fi= new JFileChooser();
        int op=fi.showOpenDialog(this);
        if(op==JFileChooser.APPROVE_OPTION){
             txtFoto.setText(fi.getSelectedFile().getPath().replace('\\', '/'));
        }
   
    }
    
    public void nuevo(){
        limpiar();
    }
    public void guardar(){
        Conexion cc= new Conexion();
        Connection cn=cc.conectar();
        String sql="";
        String codigo,nombre,marca,precio,stock,tipo,observacion,foto;
        codigo=txtCodigo.getText();
        nombre=txtNombre.getText();
        marca=txtMarca.getText();
        precio=txtPrecio.getText();
        stock=txtPrecio.getText();
        tipo=jcbTipo.getSelectedItem().toString().substring(0, jcbTipo.getSelectedItem().toString().indexOf('-'));
        observacion=txtObservacion.getText();
       
        try {
            copiar(txtFoto.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se copio correctamente la imagen");
        }
         foto=txtFoto.getText();
        sql="INSERT INTO PRODUCTO VALUES(?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement psd=cn.prepareStatement(sql);
            psd.setString(1, codigo);
            psd.setString(2, nombre);
            psd.setString(3, marca);
            psd.setString(4, precio);
            psd.setString(5, stock);
            psd.setString(6, observacion);
            psd.setString(7, tipo);
            psd.setString(8, foto);
            int n=psd.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Inserccion exitosa");
            }
            
        } catch (Exception ex) {
           JOptionPane.showMessageDialog(null, "Error de Inserccion de Productos"+ex);
        }
        
    }
    
    public void copiar(String origen) throws Exception{
        InputStream in= new FileInputStream(origen);
        OutputStream out= new FileOutputStream("C://Fotos//"+txtCodigo.getText()+".jpg");
        byte[] buffer= new byte[1024];
        int len;
        try{
            while((len=in.read(buffer))>0){
                out.write(buffer,0,len);
            }
        out.flush();
        txtFoto.setText("C://Fotos//"+txtCodigo.getText()+".jpg");
    }catch(Exception e){
        JOptionPane.showMessageDialog(null, "Error de Copiado"+e);
    }finally{
        in.close();
        out.close();
    }
        
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtMarca = new javax.swing.JTextField();
        txtObservacion = new javax.swing.JTextField();
        txtFoto = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jbtCargarImagen = new javax.swing.JButton();
        txtEntero1 = new ComponentesPropios.txtEntero();
        txtStock = new ComponentesPropios.txtEntero();
        txtPrecio = new ComponentesPropios.txtDosDeciales();
        jcbTipo = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbProductos = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("PRODUCTOS");

        jLabel2.setText("Código: ");

        jLabel4.setText("Marca: ");

        jLabel3.setText("Nombre: ");

        jLabel5.setText("Precio: ");

        jLabel6.setText("Stock: ");

        jLabel7.setText("Observación: ");

        jLabel8.setText("Tipo: ");

        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoKeyTyped(evt);
            }
        });

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        txtObservacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtObservacionActionPerformed(evt);
            }
        });

        jLabel9.setText("Foto: ");

        jbtCargarImagen.setText("Cargar Imagen");
        jbtCargarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCargarImagenActionPerformed(evt);
            }
        });

        txtEntero1.setText("txtEntero1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(5, 5, 5))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel4)))
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addGap(122, 122, 122)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtFoto, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtObservacion, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(txtNombre)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                        .addComponent(txtPrecio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jcbTipo, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addComponent(jbtCargarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(41, 41, 41)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel4)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel5)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel6)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel7)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtCargarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel10.setText("Búsqueda por Código: ");

        txtBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaActionPerformed(evt);
            }
        });
        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel10)
                .addGap(28, 28, 28)
                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtbProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtbProductos.setRowHeight(25);
        jScrollPane1.setViewportView(jtbProductos);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        );

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/CarroPrototipo.jpg"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnNuevo2.png"))); // NOI18N
        jButton1.setText("Nuevo");
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setIconTextGap(-2);
        jButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnNuevo3.png"))); // NOI18N
        jButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnNuevo.png"))); // NOI18N
        jButton1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnGuardar2.png"))); // NOI18N
        jButton2.setText("Guardar");
        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setIconTextGap(-3);
        jButton2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnGuardar3.png"))); // NOI18N
        jButton2.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnGuardar.png"))); // NOI18N
        jButton2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnActualizar2.png"))); // NOI18N
        jButton3.setText("Actualizar");
        jButton3.setBorder(null);
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setIconTextGap(-8);
        jButton3.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnActualizar3.png"))); // NOI18N
        jButton3.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnActualizar.png"))); // NOI18N
        jButton3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnBorrar2.png"))); // NOI18N
        jButton4.setText("Borrar");
        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setIconTextGap(-4);
        jButton4.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnBorrar3.png"))); // NOI18N
        jButton4.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnBorrar.png"))); // NOI18N
        jButton4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnCancelar2.png"))); // NOI18N
        jButton5.setText("Cancelar");
        jButton5.setBorder(null);
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setIconTextGap(-4);
        jButton5.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnCancelar3.png"))); // NOI18N
        jButton5.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnCancelar.png"))); // NOI18N
        jButton5.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnSalir2.png"))); // NOI18N
        jButton6.setText("Salir");
        jButton6.setBorder(null);
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setIconTextGap(-4);
        jButton6.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnSalir3.png"))); // NOI18N
        jButton6.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/BotonesDinamicos/btnSalir.png"))); // NOI18N
        jButton6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6)
                    .addComponent(jButton5))
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(133, 133, 133)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(151, 151, 151)))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void txtObservacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtObservacionActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtObservacionActionPerformed

private void txtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyTyped
// TODO add your handling code here:
}//GEN-LAST:event_txtCodigoKeyTyped

private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtNombreActionPerformed

private void txtBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtBusquedaActionPerformed

private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
consultarProductos(txtBusqueda.getText());
}//GEN-LAST:event_txtBusquedaKeyReleased

private void jbtCargarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCargarImagenActionPerformed
busqueda();
}//GEN-LAST:event_jbtCargarImagenActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Productos1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Productos1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Productos1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Productos1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Productos1().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtCargarImagen;
    private javax.swing.JComboBox jcbTipo;
    private javax.swing.JTable jtbProductos;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtCodigo;
    private ComponentesPropios.txtEntero txtEntero1;
    private javax.swing.JTextField txtFoto;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtObservacion;
    private ComponentesPropios.txtDosDeciales txtPrecio;
    private ComponentesPropios.txtEntero txtStock;
    // End of variables declaration//GEN-END:variables
}
