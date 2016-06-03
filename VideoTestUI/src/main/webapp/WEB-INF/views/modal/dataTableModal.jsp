<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link href="https://cdn.datatables.net/1.10.12/css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.datatables.net/responsive/2.1.0/css/responsive.bootstrap.min.css" rel="stylesheet">

<script	src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"	type="text/javascript"></script>
<script	src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"	type="text/javascript"></script>
<script	src="https://cdn.datatables.net/responsive/2.1.0/js/dataTables.responsive.min.js"	type="text/javascript"></script>
<script	src="https://cdn.datatables.net/responsive/2.1.0/js/responsive.bootstrap.min.js"	type="text/javascript"></script>

<!-- <link href="https://cdn.datatables.net/1.10.12/css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.datatables.net/responsive/2.1.0/css/responsive.bootstrap.min.css" rel="stylesheet">

<script	src="//code.jquery.com/jquery-1.12.3.min.js"	type="text/javascript"></script>
<script	src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"	type="text/javascript"></script>
<script	src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"	type="text/javascript"></script>
 -->

<script>

 	$(document).ready(function() {

		$('.externalList').click(function() {
			$('#externalDataTableModal').modal({
				backdrop : true
			});
			$('#example').DataTable({
				select : true
			});

		});
	}); 
</script>

<div class="modal fade" id="externalDataTableModal" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalLabel">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">External List</h4>
			</div>
			<div class="modal-body">
				<table id="example" class="table table-striped table-bordered nowrap table-hover" cellspacing="0" width="100%"  style="table-layout:fixed;">
					<thead>
						<tr>
							<th >File Name</th>
							<th >Access</th>
						<!-- 	<th>Start Date</th> -->
							<th >End Date</th>
							<!-- <th>Memo</th>
							<th>URI</th>
							<th>Security Code</th> -->
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						<tr>
							<td class="span4" style="width: 200px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">ddfbdfbdfbdfbdfbdfbdfbdfbdfbdfb</td>
							<td class="span2" style="width: 50px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">Nixon</td>
							<td class="span1" style="width: 80px; overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">2000-01-01 11:11</td>
						</tr>
						
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
