import TableCell from "@mui/material/TableCell";

export default function CellOrInput(props)
{


    return( <>
        { props.tagType ?
            <TableCell align="center" > props.content </TableCell>
             :<input type={"text"} value={props.content}/> }
    </>)
}