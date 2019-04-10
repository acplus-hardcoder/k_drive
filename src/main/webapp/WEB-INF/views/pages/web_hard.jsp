<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="container" style="margin:15px">
	<div class='row'>		
		<div class="col-sm-4" style="text-align:left;">			
			<div id="treeview-left" style="font-size: 16px; background-color: #f0f0f0; 
				padding: 10px; height: 500px; overflow: auto"></div>
			<h5>Drag + Ctrl Key : Copy</h5>		    
		</div>
		<div class="col-sm-8">
			<div id="treeview-right" style="padding-top: 13px; font-size: 16px; 
				height: 500px; overflow: auto; background-color: #f0f0f0;">
			</div>			
			<ul class="options" style="margin-top: 5px;">
				<li>
					<input id="append-node-name" value="이름" style="width:100px;"/>
					<label class="btn btn-success">폴더 생성					
					<input type="button" id="append-node" style="display: none;">
					</label>
					<label class="btn btn-danger">선택 삭제
					<input type="button" id="delete-node" style="display: none;">
					</label>															
					<label class="btn btn-primary btn-file">파일업로드
					<input type="file" style="display: none;" name="file" id="file" multiple>
					</label>					
					<label class="btn btn-info btn-file">폴더업로드
					<input type="file" style="display: none;" name="file" 
						id="folder" multiple webkitdirectory>
					</label>										
					<label class="btn btn-secondary">선택 다운로드
					<input type="button" style="display: none;" id="select-down">
					</label>					
				</li>				
			</ul>			
		</div>		
	</div>		
</div>

<script>
$(document).ready(function() {	
	var treeview = $("#treeview-left").kendoTreeView({
	    dragAndDrop: true,
	    autoScroll: true,
	    select: onSelect,	    
	    drop: onDrop,
	    drag: onDrag,
	    loadOnDemand: false
    }).data("kendoTreeView");
	var treeview2 = $("#treeview-right").kendoTreeView({
		checkboxes: {
            checkChildren: false
        },
		check: onCheck,		
	    autoScroll: true,	    
	    loadOnDemand: false
    }).data("kendoTreeView");
			
	var lst = <%= request.getAttribute("klist") %>;
	var user_num = <%= request.getAttribute("user_num") %>;
	var user_name = '<%= request.getAttribute("user_name") %>';
	$("#loginout").empty();
	$("#loginout").append('<a href="/k_drive/logout">' +
		'<span class="glyphicon glyphicon-log-in"></span>' + user_name + ' Logout</a>');	
	var dataItem, element, str, array;	

	var json = new Array();
	make_json("", json, lst);
	
	function make_json(id, object, source) {		
		for(var i = 0; i < source.length; i++) {			
			if(source[i].O_PARENT == id) {
				var obj = new Object();
				obj.text = source[i].O_NAME;
				obj.id = source[i].O_NUM;
				if(source[i].O_PARENT > 0) {					
					obj.imageUrl = "img/image_folder.png";										
				}
				else {					
					obj.imageUrl = "img/image_pc.png";								
				}
				obj.items = new Array();				
				make_json(obj.id, obj.items, source);
				object.push(obj);
			}				
		}
	}	
	
	treeview.setDataSource(new kendo.data.HierarchicalDataSource({
		data: json
	}));
	dataItem = treeview.dataSource.get(1);
	if(dataItem) {
		element = treeview.findByUid(dataItem.uid);
		treeview.select(element);
		treeview.expand(element);
		k_select(dataItem);
	}
	
	function onCheck() {		
        var checkedNodes = [],
        	treeView = $("#treeview-right").data("kendoTreeView"),
            message;

        checkedNodeIds(treeView.dataSource.view(), checkedNodes);

        if (checkedNodes.length > 0) {
            message = "IDs of checked nodes: " + checkedNodes.join(",");
        } else {
            message = "No nodes checked.";
        }
        console.log(message);
    }
	
	function checkedNodeIds(nodes, checkedNodes) {
        for (var i = 0; i < nodes.length; i++) {
            if (nodes[i].checked) {
                checkedNodes.push(nodes[i].id);
            }            
        }
    }
	
	$("#delete-node").click(function(e) {
		var checkedNodes = [];
		var treeView = $("#treeview-right").data("kendoTreeView");
		var node;
		var dataItem;		

	    checkedNodeIds(treeView.dataSource.view(), checkedNodes);
		
	    if(checkedNodes.length <= 0) return;
	    
	    for (var i = 0; i < checkedNodes.length; i++) {	    	
	    	$.post("http://10.0.0.46:8080/k_drive/kdrive_del?num=" + checkedNodes[i] + 
	 			   "&table=" + user_num);	 		
	 		dataItem = treeview2.dataSource.get(checkedNodes[i]);
	    	node = treeview2.findByUid(dataItem.uid);
	 		treeview2.remove(node);
	 		
	 		dataItem = treeview.dataSource.get(checkedNodes[i]);
	 		if(dataItem != null) {	 				 			
		    	node = treeview.findByUid(dataItem.uid);
		 		treeview.remove(node);
	 		}
	 			
	    }	    
	});
		
	$("#append-node").click(function(e) {		
		var name = $("#append-node-name").val();
		
		var node = treeview.select();
		var dataItem = treeview.dataItem(node);
		treeview.expand(node);
		
		$.post("http://10.0.0.46:8080/k_drive/kdrive_md?num=" + dataItem.id + 
			   "&name=" + name + "&table=" + user_num, function(list) {
			if(dataItem.items) {
				for(var i = 0; i < dataItem.items.length; i++) {
					if(dataItem.items[i].text > name) {
						node = treeview.findByUid(dataItem.items[i].uid);					
						treeview.insertBefore({
							text: name, id: list[0].S_NUM,
							imageUrl: "img/image_folder.png"
						}, node);
						k_select(dataItem);					
						return;
					}
				}
			}
			treeview.append({
				text: name, id: list[0].S_NUM, imageUrl: "img/image_folder.png" 			
			}, node);			
			k_select(dataItem);
		});		
	});
		
	function k_select(e)
	{
		$.post("http://10.0.0.46:8080/k_drive/kdrive_select?num="
	    		+ e.id + "&table=" + user_num, function(list) {
   			var json = new Array();   			
			for (var i in list) {
				l = list[i];
				var obj = new Object();				
				if(l.O_DNF == 0) {
					obj.text = l.O_NAME + " (" + l.O_DATE + ")";
					obj.imageUrl = "img/image_folder.png";										
				}
				else {
					obj.text = l.O_NAME + " (" + l.O_DATE + ")";
					obj.imageUrl = "img/image_file.png";
					obj.filename = l.O_NAME;
					obj.dbfilename = l.O_DBNAME;
				}				
				obj.id = l.O_NUM;				
				json.push(obj);      			
   			}
			treeview2.setDataSource(new kendo.data.HierarchicalDataSource({				
				data: json
			}));   					
	    });		
	}

	function onSelect(e) {	    
	    var element = treeview.dataItem(e.node);	    
	    k_select(element);	    		
	}
	
	function onDragStart(e) {}
	
	function onDrag(e) {
		var end = treeview.dataItem(e.dropTarget);		
		if (e.statusClass.indexOf("insert") == 2 || end == undefined) {
		    // deny the operation
		    e.setStatusClass("k-i-cancel");		    		    
		}		
		else if(!e.originalEvent.ctrlKey && e.statusClass.indexOf("plus") == 2) {
			e.setStatusClass("k-i-insert-middle");
			treeview.expand(e.dropTarget);
		}
		else if(e.statusClass.indexOf("plus") == 2) {			
			treeview.expand(e.dropTarget);
		}
	}
	
	function onDrop(e) {		
		if(e.valid) {			
			var start = treeview.dataItem(e.sourceNode);
			var end = treeview.dataItem(e.dropTarget);
						
			if(e.originalEvent.ctrlKey) {
				var node;
				$.post("http://10.0.0.46:8080/k_drive/kdrive_copy?source=" + start.id
					+ "&target=" + end.id + "&table=" + user_num, function(list) {					
					var json = new Array();
					make_json(end.id, json, list);
										
					if(end.items) {
						for(var i = 0; i < end.items.length; i++) {
							if(end.items[i].text > list[0].O_NAME) {
								node = treeview.findByUid(end.items[i].uid);
								treeview.insertBefore(json, node);
								e.setValid(false);
								treeview.select(e.dropTarget);
								k_select(end);								
								return;
							}
						}						
						node = treeview.findByUid(end.uid);
						treeview.append(json, node);
						treeview.select(node);
						k_select(end);
					}
					else {
						node = treeview.findByUid(end.uid);
						treeview.append(json, node);
						treeview.select(node);
						k_select(end);
					}
				});
				e.setValid(false);
			}
			else {
				var node;
				$.post("http://10.0.0.46:8080/k_drive/kdrive_move?source=" + start.id
						+ "&target=" + end.id + "&table=" + user_num, function() {
					k_select(end);
				});
				if(end.items) {
					for(var i = 0; i < end.items.length; i++) {					
						if(end.items[i].text > start.text) {
							node = treeview.findByUid(end.items[i].uid);						
							treeview.insertBefore(e.sourceNode, node);						
							e.setValid(false);
							break;
						}
					}
				}
				treeview.select(e.dropTarget);
			}
		}
	}

	document.getElementById('folder').onchange = function(e) {
		var node = treeview.select();
		var dataItem = treeview.dataItem(node);
		var files = e.target.files; // FileList		
		var directory = new Array();
		var formData = new FormData();
		
		for (var i = 0, f; f = files[i]; ++i) {			
			//directory.push(files[i].webkitRelativePath);		
			formData.append("file", files[i]);
			formData.append("dir", files[i].webkitRelativePath)
		}		
		
	    $.ajax({
	        type: "POST",
	        enctype: 'multipart/form-data',
	        url: "http://10.0.0.46:8080/k_drive/upload_folder?table=" + user_num +
	        		"&target=" + dataItem.id,// + "&dir=" + directory,
	        data: formData,
	        processData: false,
	        contentType: false,
	        cache: false,	        
	        success: function (data) {
	        	var json = new Array();	        	 
				make_json(dataItem.id, json, data);
				var name = json[0].text;
	        	if(dataItem.items) {
					for(var i = 0; i < dataItem.items.length; i++) {
						if(dataItem.items[i].text > name) {
							node = treeview.findByUid(dataItem.items[i].uid);					
							treeview.insertBefore(json, node);
							k_select(dataItem);
							return;
						}
					}					
					treeview.append(json, node);
					k_select(dataItem);
				}
	        },
	        error: function (e) {
	            console.log("ERROR : ", e);
	        }
	    });				
	}
	
	document.getElementById('file').onchange = function(e) {
		var node = treeview.select();
		var dataItem = treeview.dataItem(node);
		var files = e.target.files; // FileList		
		var directory = new Array();
		var formData = new FormData();
		
		for (var i = 0, f; f = files[i]; ++i) {			
			formData.append("file", files[i]);
		}		
		
	    $.ajax({
	        type: "POST",
	        enctype: 'multipart/form-data',
	        url: "http://10.0.0.46:8080/k_drive/upload_file?table=" + user_num + 
	        		"&target=" + dataItem.id,
	        data: formData,
	        processData: false,
	        contentType: false,
	        cache: false,	        
	        success: function (data) {	            
	            for (var i in data) {
					l = data[i];
					if(l.result == "true")
						k_select(dataItem);
	            }	            
	        },
	        error: function (e) {
	            console.log("ERROR : ", e);
	            alert("업로드 실패!")
	        }
	    });
	}
	
	$("#select-down").click(function(e) {
		var checkedNodes = [];
		var treeView = $("#treeview-right").data("kendoTreeView");
		var node;
		var dataItem;		

	    checkedNodeIds(treeView.dataSource.view(), checkedNodes);
		
	    if(checkedNodes.length <= 0) return;
	    
	    for (var i = 0; i < checkedNodes.length; i++) {
	    	dataItem = treeview2.dataSource.get(checkedNodes[i]);
	    	console.log(dataItem.filename + ":" + dataItem.dbfilename);
	    	location.href = "/k_drive/down?filename="+dataItem.filename+"&dbname="+dataItem.dbfilename+"&table=" + user_num;
	    	/*$.get("http://10.0.0.46:8080/k_drive/down?filename=" + dataItem.filename + 
	 			   "&dbname=" + dataItem.dbfilename + "&table=" + user_num, function(e) {
	    		console.log("down success");	    				 		
	 		});*/
	    }
	});
});
</script>