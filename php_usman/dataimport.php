<?php
require_once "conn.php";
$url = file_get_contents("record.json");
$progurl = json_decode($url);
echo count($progurl[0]);
print_r($progurl[0]);
echo $record1 = $progurl[0]->time;
$arr = array("time","client_title","made_on_behalf","country","email_address","phone","additional_info","special_request_made","hotelname","amount","checkin","checkout","cab_service_req","number_of_rooms","is_phone_booking","hotel_state","ip_address","cancelled","customer_cancelled_by_self");
$arr2 = array("time","client_title","made_on_behalf","country","email_address","phone","additional_info","special_request_made",
"hotelname","amount","checkin","checkout","cab_service_req","number_of_rooms","is_phone_booking","hotel_state","ip_address","cancelled","customer_cancelled_by_self");					
					$count = 1;
					foreach($progurl as $key=>$value)
					{
						
						if($count > 3000)
						{
							
					
					$query = "insert into testrecord(";
					$countrec = 0;
					foreach($arr as $key=>$value2)
					{
						$query.="$value2".",";
						$countrec++;
					}
					$query =  substr($query,0,strlen($query)-1);
					$query.= ")values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
					$query = sprintf($query,
					mysqli_real_escape_string($conn,$value->$arr2[0]),
					mysqli_real_escape_string($conn,$value->$arr2[1]),
					mysqli_real_escape_string($conn,$value->$arr2[2]),
					mysqli_real_escape_string($conn,$value->$arr2[3]),
					mysqli_real_escape_string($conn,$value->$arr2[4]),
					mysqli_real_escape_string($conn,$value->$arr2[5]),
					mysqli_real_escape_string($conn,$value->$arr2[6]),
					mysqli_real_escape_string($conn,$value->$arr2[7]),
					mysqli_real_escape_string($conn,$value->$arr2[8]),
					mysqli_real_escape_string($conn,$value->$arr2[9]),
					mysqli_real_escape_string($conn,$value->$arr2[10]),
					mysqli_real_escape_string($conn,$value->$arr2[11]),
					mysqli_real_escape_string($conn,$value->$arr2[12]),
					mysqli_real_escape_string($conn,$value->$arr2[13]),
					mysqli_real_escape_string($conn,$value->$arr2[14]),
					mysqli_real_escape_string($conn,$value->$arr2[15]),
					mysqli_real_escape_string($conn,$value->$arr2[16]),
					mysqli_real_escape_string($conn,$value->$arr2[17]),
					mysqli_real_escape_string($conn,$value->$arr2[18])
					);
					
					mysqli_query($conn,$query) or die(mysqli_error($conn));
					
						}
						$count++;
					}
					//mysqli_real_escape_string($conn,$progurl[1])";
					/*
					
									mysqli_real_escape_string($conn,$rideid));
									$resultjoin2 = mysqli_query($conn,$query);
								@$numrow = mysqli_num_rows($resultjoin2);
								*/
								
mysqli_close($conn);					
?>