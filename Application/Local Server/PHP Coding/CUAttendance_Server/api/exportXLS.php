<?php 
header('Content-type: text/xml');
header("Content-Disposition: attachment; filename=export.xml");
print "<?xml version=\"1.0\"?>\n";
print "<?mso-application progid=\"Excel.Sheet\"?>\n";
include_once './db_functions.php';
$db = new DB_Functions(basename(__FILE__, '.php'));
$rows = array();
$rows = $db->getAllData();

?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
    xmlns:o="urn:schemas-microsoft-com:office:office"
    xmlns:x="urn:schemas-microsoft-com:office:excel"
    xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
    xmlns:html="http://www.w3.org/TR/REC-html40">
    <DocumentProperties
    xmlns="urn:schemas-microsoft-com:office:office">
    <Author>Jack Herrington</Author>
    <LastAuthor>Jack Herrington</LastAuthor>
    <Created>2005-08-02T04:06:26Z</Created>
    <LastSaved>2005-08-02T04:30:11Z</LastSaved>
    <Company>My Company, Inc.</Company>
    <Version>11.6360</Version>
    </DocumentProperties>
    <ExcelWorkbook
    xmlns="urn:schemas-microsoft-com:office:excel">
    <WindowHeight>8535</WindowHeight>
    <WindowWidth>12345</WindowWidth>
    <WindowTopX>480</WindowTopX>
    <WindowTopY>90</WindowTopY>
    <ProtectStructure>False</ProtectStructure>
    <ProtectWindows>False</ProtectWindows>
    </ExcelWorkbook>
    <Styles>
    <Style ss:ID="Default" ss:Name="Normal">
    <Alignment ss:Vertical="Bottom"/>
    <Borders/>
    <Font/>
    <Interior/>
    <NumberFormat/>
    <Protection/>
    </Style>
    <Style ss:ID="s21" ss:Name="Hyperlink">
    <Font ss:Color="#0000FF" ss:Underline="Single"/>
    </Style>
    <Style ss:ID="s23">
    <Font x:Family="Swiss" ss:Bold="1"/>
    </Style>
    </Styles>
    <Worksheet ss:Name="Names">
    <Table ss:ExpandedColumnCount="4"
    ss:ExpandedRowCount="<?php echo( count( $rows ) + 1 ); ?>"
    x:FullColumns="1" x:FullRows="1">
    <Column ss:Index="4" ss:AutoFitWidth="0" ss:Width="154.5"/>
    <Row ss:StyleID="s23">
    <Cell><Data
    ss:Type="String">Name</Data></Cell>
    <Cell><Data
    ss:Type="String">Phone</Data></Cell>
    <Cell><Data
    ss:Type="String">Mut ID</Data></Cell>
    </Row>
    <?php foreach( $rows as $row ) { ?>
    <Row>
    <Cell><Data
    ss:Type="String"><?php echo( $row['name'] ); ?>
    </Data></Cell>
    <Cell><Data
    ss:Type="String"><?php echo( $row['phone'] ); ?>
    </Data></Cell>
    <Cell><Data
    ss:Type="String"><?php echo( $row['mut_id'] ); ?>
    </Data></Cell>
    </Row>
    <?php } ?>
    </Table>
    <WorksheetOptions
    xmlns="urn:schemas-microsoft-com:office:excel">
    <Print>
    <ValidPrinterInfo/>
    <HorizontalResolution>300</HorizontalResolution>
    <VerticalResolution>300</VerticalResolution>
    </Print>
    <Selected/>
    <Panes>
    <Pane>
    <Number>3</Number>
    <ActiveRow>1</ActiveRow>
    </Pane>
    </Panes>
    <ProtectObjects>False</ProtectObjects>
    <ProtectScenarios>False</ProtectScenarios>
    </WorksheetOptions>
    </Worksheet>
    </Workbook>
