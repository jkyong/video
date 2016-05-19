<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<style>
.table tbody>tr>th {
	vertical-align: middle;
}
.table tbody>tr>td {
	vertical-align: middle;
}
.table tbody>tr>td>div>ul>li>a {
	cursor: pointer;
}
</style>
<!-- new folder modal  -->
<div class="modal fade" id="newFolderModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">New Folder</h4>
			</div>
			<div class="modal-body">
				<div id="alreadyNameFolder" class="alert alert-danger" style="display: none;">
  					<strong> folder name already exists. </strong>
				</div>
				<div id="whiteSpaceFolder" class="alert alert-danger" style="display: none;">
  					<strong> please input the folder name. </strong>
				</div>
				<form id="newFolderForm">
					<input type="text" class="form-control newFolderInput" placeholder="input the new folder name.">
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-primary createNewFolder">Create</button>
			</div>
		</div>
	</div>
</div>

<!-- rename modal  -->
<div class="modal fade" id="renameModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">Rename</h4>
			</div>
			<div class="modal-body">
				<div id="alreadyNameRename" class="alert alert-danger" style="display: none;">
  					<strong> file name already exists. </strong>
				</div>
				<div id="whiteSpaceRename" class="alert alert-danger" style="display: none;">
  					<strong> please input the file name. </strong>
				</div>
				<form id="renameForm">
					<input type="text" class="form-control renameInput" placeholder="rename the file name.">
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-primary nameChange">Change</button>
			</div>
		</div>
	</div>
</div>

<!-- move modal -->
<div class="modal fade" id="moveModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">Directory Move</h4>
			</div>
			<div class="modal-body">
				<div id="moveTree"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary moveModalMove">Move</button>
			</div>
		</div>
	</div>
</div>

<!-- upload modal -->
<div class="modal fade" id="uploadModal" role="dialog">
	<div class="modal-dialog modal-md">
		<div class="modal-content">			
			<div class="modal-header">
				<h4 class="modal-title" id="exampleModalLabel">Upload</h4>
			</div>
			<div class="modal-body" > 
				<div id="fileuploader">Select</div>
			</div> 
			<div class="modal-footer">
				<button type="button" class="btn btn-default uploadCancel" data-dismiss="modal">Cancel</button>
				<button id="extrabutton" type="button" class="btn btn-success startUpload">Start Upload</button>
			</div>
		</div>
	</div>
</div>

<!-- remove modal  -->
<div class="modal fade" id="removeModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">Remove</h4>
			</div>
			<div class="modal-body"> 
  				Are you sure you want to remove?
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-danger removeRemove">Remove</button>
			</div>
		</div>
	</div>
</div>

<!-- URI & PW modal -->
<div class="modal fade" id="passModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">URI & Account</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label for="recipient-name" class="control-label">URI:</label>
					<div id="uri"></div>
				</div>
				<div class="form-group">
					<label for="message-text" class="control-label">Security Code:</label>
					<div id="external"></div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
    
    
<!-- test table modal -->
<div class="modal fade" id="externalModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">External</h4>
			</div>
			<div class="modal-body">
				<table class="table table-md">
					<tbody>
						<tr>
							<th>Access</th>
							<td>
								<div class="dropdown">
									<button class="btn btn-default dropdown-toggle dropdownBtn" type="button"
										id="dropdownMenu" data-toggle="dropdown" aria-haspopup="true"
										aria-expanded="true">
										private <span class="caret"></span>
									</button>
									<ul class="dropdown-menu" aria-labelledby="dropdownMenu">
										<li><a>private</a></li>
										<li><a>public</a></li>
<!-- 										<li role="separator" class="divider"></li> -->
									</ul>
								</div>
							</td>						
						</tr>
						<tr>
							<th>Memo</th>
							<td>
								<input type="text" class="form-control memo" placeholder="" >							
							</td>
						</tr>
						<tr class="defaultResults">
							<th>Start Date</th>
							<td>
								<div id="startDate" class="resultDate"></div>
							</td>
						</tr>
						<tr class="defaultResults">
							<th>End Date</th>
							<td>
								<div id="endDate" class="resultDate"></div>
							</td>						
						</tr>
						<tr class="defaultResults">
							<th>URI</th>
							<td>
								<div id="uriCreate" class="resultURI"></div>
							</td>						
						</tr>
						<tr class="securityResult">
							<th>Security Code</th>
							<td>
								<div id="securityCode" class="resultSecurityCode"></div>
							</td>						
						</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary externalCreate" >Create</button>
			</div>
		</div>
	</div>
</div>