<?php
include_once './db_functions.php';
$df = new DB_Functions(basename(__FILE__, '.php'));
$data = array();
$data = $df->getAllData();
?>
<html>
<body>
	<table>
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Phone</th>
			<th>Mut ID</th>
		</tr>
 <?php
foreach ($data as $d) {
    ?>
 <tr>
			<td><?php echo( $d['id'] ); ?></td>
			<td><?php echo( $d['name'] ); ?></td>
			<td><?php echo( $d['phone'] ); ?></td>
			<td><?php echo( $d['mut_id'] ); ?></td>
		</tr>
 <?php } ?>
 </table>
	Download as an
	<a href="exportXLS.php">Excel spreadsheet</a>.
</body>
</html>