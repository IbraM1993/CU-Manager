<?php

	file_put_contents('admin_import.txt', print_r('start' , true));
 function admin_import() {
	file_put_contents('admin_import.txt', print_r('start admin_import ' , true));
    include_once 'db_functions.php';
    //$data =  file_get_contents('php://input');
  if (isset($_REQUEST['upload'])) {
    $ok = true;
    $file = $_FILES['csv_file']['tmp_name'];
    $handle = fopen($file, "r");
    if ($file == NULL) {
      error(_('Please select a file to import'));
      redirect(page_link_to('admin_export'));
    }
    else {
      while(($filesop = fgetcsv($handle, 1000, ",")) !== false)
        {
			$newuser = array();
		
          $newuser["name"] 	= $filesop[0];
          $newuser["phone"] = $filesop[1];
          $newuser["mut_id"]= $filesop[2];
          
 // If the tests pass we can insert it into the database.       
        
		$test = $df->inserttodb($newuser);
			
       
      
      if ($sql) {
        success(_("You database has imported successfully!"));
        redirect(page_link_to('admin_export'));
      } else {
        error(_('Sorry! There is some problem in the import file.'));
        redirect(page_link_to('admin_export'));
        }
    }
  }
//form_submit($name, $label) Renders the submit button of a form
//form_file($name, $label) Renders a form file box

 return page_with_title("Import Data", array(
   msg(),
  div('row', array(
          div('col-md-12', array(
              form(array(
                form_file('csv_file', _("Import user data from a csv file")),
                form_submit('upload', _("Import"))
              ))
          ))
      ))
  ));
}
}

?>