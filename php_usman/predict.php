<?php
if(isset($_GET['email']))
{
	require_once "conn.php";
	$email = $_GET['email'];
	$newCheckIn = "2015-08-01 00:00:00";
	$behalf = 0;
	//Distance of the hotel
		if($behalf == 0)
		{
		$sql = "select * from testrecord where email_address = '%s' and cancelled = 0 and (checkin <= '$newCheckIn' and checkout >= '$newCheckIn')";
		$sql = sprintf($sql,
		mysqli_real_escape_string($conn,$email)
		);
		$res = mysqli_query($conn,$sql) or die(mysqli_error($conn));
		echo @mysqli_num_rows($res);
		}
		else if($behalf == 1)
		{
			echo "no enough record to predict if customer is likely to cancel since we don't have the data of who they are booking for";
			/*
			$sql = "select * from testrecord where email_address = '%s' and cancelled = 0 and checkin <= '$newCheckIn' and checkout >= '$newCheckIn'";
		$sql = sprintf($sql,
		mysqli_real_escape_string($conn,$email)
		);
		$res = mysqli_query($conn,$sql) or die(mysqli_error($conn));
		echo @mysqli_num_rows($res);
		*/
		}
	mysqli_close($conn);
}
else
{
		echo "Please enter email variable as GET";
}
?>