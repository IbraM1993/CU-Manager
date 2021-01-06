<?php

include_once 'db_functions.php';
$data = array();
if ($_FILES['file']['tmp_name']) {
    $dom = new DOMDocument();
    $dom->load($_FILES['file']['tmp_name']);

    $rows = $dom->getElementsByTagName('Row');
    $first_row = true;
    file_put_contents('Rows Count.txt', print_r("Count : ".count($rows), true));
    foreach ($rows as $row) {
        if (! $first_row) {
            $name = "";
            $phone = "";
            $mut_id = "";
            
            $index = 1;
            $cells = $row->getElementsByTagName('Cell');
            foreach ($cells as $cell) {
                $ind = $cell->getAttribute('Index');
                if ($ind != null)
                    $index = $ind;
                if ($index == 1)
                    $name = $cell->nodeValue;
                if ($index == 2)
                    $phone = $cell->nodeValue;
                if ($index == 3)
                    $mut_id = $cell->nodeValue;
                
                $index += 1;
            }
            // ADD NEW ROW
            $data[] = array(
                'name' => $name,
                'phone' => $phone,
                'mut_id' => $mut_id
            );
        }
        $first_row = false;
    }
    $df = new DB_Functions(basename(__FILE__, '.php'));
    $test = $df->insertRowData($data);
}
?>
<html>
<body>
	<table>
		<tr>
			<th>Name</th>
			<th>Phone</th>
			<th>Mut ID</th>
		</tr>
<?php foreach( $data as $row ) { ?>
<tr>
			<td><?php echo( $row['name'] ); ?></td>
			<td><?php echo( $row['phone'] ); ?></td>
			<td><?php echo( $row['mut_id'] ); ?></td>
		</tr>
<?php } ?>
</table>
Click <a href="list_all.php">here</a> for the entire table.
</body>
</html>