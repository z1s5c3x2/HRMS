import TableCell from "@mui/material/TableCell";

export default function CellOrInput(props)
{
    return( <>
        { props.tagType ?
            <TableCell align="center" > props.content </TableCell>
             :<input style={{margin:"0 auto"}} type={"text"} value={props.content}/> }
    </>)
}